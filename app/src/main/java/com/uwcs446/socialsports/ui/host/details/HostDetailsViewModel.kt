package com.uwcs446.socialsports.ui.host.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchLocation
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.domain.user.CurrentUserRepository
import com.uwcs446.socialsports.domain.user.User
import com.uwcs446.socialsports.services.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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

    private val selectedMatchLocation = state.get<MatchLocation>("matchLocation")
    private val editMatchId = state.get<String>("matchId")

    // Mock match data TODO: Get match model from matchRepository by matchId
    private var editMatch = editMatchId?.let {
        Match(
            id = "id_of_existing_match",
            sport = Sport.SOCCER,
            title = "title_of_existing_match",
            description = "discription_of_existing_match",
            date = LocalDate.now(),
            time = LocalTime.now(),
            duration = Duration.parse("PT8H"),
            host = User("testUser"),
            location = MatchLocation("", LatLng(0.0, 0.0)),
            teamOne = listOf("1"),
            teamTwo = listOf("1")
        )
    }

    var matchTitle = editMatch?.title ?: ""
    var sportType = editMatch?.sport ?: ""
    var matchDate = editMatch?.date?.toString() ?: ""
    var matchTime = editMatch?.time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""
    var matchDurationHour = editMatch?.duration?.toHours() ?: ""
    var matchDurationMinute = editMatch?.duration?.toMinutes()?.rem(60) ?: ""
    var matchDescription = editMatch?.description ?: ""
    var user = currentUserRepository.getUser()

    // Setup location card
    var locationId = selectedMatchLocation?.placeId ?: editMatch?.location?.placeId ?: ""
    private val _locationName = MutableLiveData<String>().apply { value = "" }
    val locationName: LiveData<String> = _locationName
    private val _locationAddress = MutableLiveData<String>().apply { value = "" }
    val locationAddress: LiveData<String> = _locationAddress
    init {
        viewModelScope.launch {
            try {
                val response = locationService.getPlace(locationId!!).await()
                _locationName.value = response.place.name
                _locationAddress.value = response.place.address
            } catch (e: Exception) {
                Log.e(TAG, "Something went wrong while fetching place response", e)
            }
        }
    }

    fun onSaveClick() {
        val durationHour = Duration.ofHours(
            if (matchDurationHour == "") 0 else matchDurationHour.toString().toLong()
        )
        val durationMin = Duration.ofMinutes(
            if (matchDurationMinute == "") 0 else matchDurationMinute.toString().toLong()
        )
        if (editMatchId != null) {
            val updatedMatch = editMatch?.copy(
                title = matchTitle,
                sport = Sport.valueOf(sportType.toString()),
                date = LocalDate.parse(matchDate),
                time = LocalTime.parse(matchTime),
                location = selectedMatchLocation!!,
                duration = durationHour + durationMin,
                description = matchDescription
            )
            matchRepository.edit(updatedMatch!!)
        } else {
            val newMatch = Match(
                id = UUID.randomUUID().toString(),
                title = matchTitle,
                sport = Sport.valueOf(sportType.toString()),
                date = LocalDate.parse(matchDate),
                time = LocalTime.parse(matchTime),
                location = selectedMatchLocation!!,
                duration = durationHour + durationMin,
                description = matchDescription,
                host = user!!,
                teamOne = emptyList(),
                teamTwo = emptyList()
            )
            matchRepository.create(newMatch)
        }
    }
}
