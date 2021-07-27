package com.uwcs446.socialsports.ui.host

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.datetimepicker.DateTimePicker
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchLocation
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.domain.user.CurrentUserRepository
import com.uwcs446.socialsports.services.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HostDetailsViewModel @Inject constructor(
    private val currentUserRepository: CurrentUserRepository,
    private val matchRepository: MatchRepository,
    private val state: SavedStateHandle,
    private val locationService: LocationService
) : ViewModel() {
    private val TAG = this::class.simpleName

    var user = currentUserRepository.getUser()

    // Pre-fill the form with existing match information for match editing flow
    private val editMatch = state.get<Match>("match")
    /* // Test data for editMatch
    val editMatch = Match(
        id = "1",
        title = "matchTitle",
        sport = Sport.valueOf("SOCCER"),
        date = LocalDate.parse("2021-07-10"),
        time = LocalTime.parse("12:30:00"),
        location = MatchLocation("ChIJl6dkQKv2K4gRqZLt-DxGG7U", LatLng(0.0,0.0)),
        duration = Duration.parse("PT2H"),
        description = "matchDescription",
        host = user!!,
        teamOne = emptyList(),
        teamTwo = emptyList()
    ) */
    private var locationId = editMatch?.location?.placeId ?: ""
    private val _locationName = MutableLiveData<String>().apply { value = "" }
    val locationName: LiveData<String> = _locationName
    private val _locationAddress = MutableLiveData<String>().apply { value = "" }
    val locationAddress: LiveData<String> = _locationAddress
    private var _editMatchFlow = MutableLiveData<Boolean>(false)
    val editMatchFlow: LiveData<Boolean> = _editMatchFlow
    init {
        viewModelScope.launch {
            try {
                if (editMatch != null) {
                    _editMatchFlow.value = true
                    val response = locationService.getPlace(locationId!!).await()
                    _locationName.value = response.place.name
                    _locationAddress.value = response.place.address
                }
            } catch (e: Exception) {
                Log.e(TAG, "Something went wrong while fetching place response", e)
            }
        }
    }

    var matchLocation = editMatch?.location ?: MatchLocation("", LatLng(0.0, 0.0))
    var matchTitle = editMatch?.title ?: ""
    var sportType = editMatch?.sport ?: ""
    var matchDate = editMatch?.date?.format(DateTimePicker().getDateFormatter()) ?: ""
    var matchTime = editMatch?.time?.format(DateTimePicker().getTimeFormatter()) ?: ""
    var matchDurationHour = editMatch?.duration?.toHours() ?: ""
    var matchDurationMinute = editMatch?.duration?.toMinutes()?.rem(60) ?: ""
    var matchDescription = editMatch?.description ?: ""

    fun onSaveClick() {
        val durationHour = Duration.ofHours(
            if (matchDurationHour == "") 0 else matchDurationHour.toString().toLong()
        )
        val durationMin = Duration.ofMinutes(
            if (matchDurationMinute == "") 0 else matchDurationMinute.toString().toLong()
        )
        if (editMatch != null) {
            val updatedMatch = editMatch?.copy(
                title = matchTitle,
                sport = Sport.valueOf(sportType.toString()),
                date = LocalDate.parse(matchDate, DateTimePicker().getDateFormatter()),
                time = LocalTime.parse(matchTime, DateTimePicker().getTimeFormatter()),
                location = matchLocation,
                duration = durationHour + durationMin,
                description = matchDescription
            )
            matchRepository.edit(updatedMatch!!)
        } else {
            val newMatch = Match(
                id = UUID.randomUUID().toString(),
                title = matchTitle,
                sport = Sport.valueOf(sportType.toString()),
                date = LocalDate.parse(matchDate, DateTimePicker().getDateFormatter()),
                time = LocalTime.parse(matchTime, DateTimePicker().getTimeFormatter()),
                location = matchLocation,
                duration = durationHour + durationMin,
                description = matchDescription,
                hostId = user!!.id,
                teamOne = emptyList(),
                teamTwo = emptyList()
            )
            matchRepository.create(newMatch)
        }
    }
}
