package com.uwcs446.socialsports.ui.host

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.location.Location
import com.uwcs446.socialsports.domain.location.LocationRepository
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HostDetailsViewModel @Inject constructor(
    private val currentUserRepository: CurrentAuthUserRepository,
    private val matchRepository: MatchRepository,
    private val locationRepository: LocationRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    private val TAG = this::class.simpleName

    var user = currentUserRepository.getUser()

    // Pre-fill the form with existing match information for match editing flow
    private val editMatch = state.get<Match>("match")
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
                    val place = locationRepository.getPlace(locationId!!)
                    _locationName.value = place.name
                    _locationAddress.value = place.address
                }
            } catch (e: Exception) {
                Log.e(TAG, "Something went wrong while fetching place response", e)
            }
        }
    }

    var matchLocation = editMatch?.location ?: Location("", LatLng(0.0, 0.0))
    var matchTitle = editMatch?.title ?: ""
    var sportType = editMatch?.sport ?: ""
    var matchStartTime = editMatch?.startTime
    var matchEndTime = editMatch?.endTime
    var matchDescription = editMatch?.description ?: ""

    fun onSaveClick() {
        if (editMatch != null) {
            val updatedMatch = editMatch.copy(
                title = matchTitle,
                sport = Sport.valueOf(sportType.toString()),
                startTime = matchStartTime!!,
                endTime = matchEndTime!!,
                location = matchLocation,
                description = matchDescription
            )
            matchRepository.edit(updatedMatch)
        } else {
            val newMatch = Match(
                id = UUID.randomUUID().toString(),
                title = matchTitle,
                sport = Sport.valueOf(sportType.toString()),
                description = matchDescription,
                startTime = matchStartTime!!,
                endTime = matchEndTime!!,
                location = matchLocation,
                hostId = user!!.uid,
                teamOne = emptyList(),
                teamTwo = emptyList(),
                blacklist = emptyList()
            )
            matchRepository.create(newMatch)
        }
    }
}
