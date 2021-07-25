package com.uwcs446.socialsports.ui.host.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.match.MatchLocation
import com.uwcs446.socialsports.domain.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HostLocationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _locationSuggestions = MutableLiveData<List<MatchLocation>>().apply {
        // TODO[BACKEND]: get location suggestions for current user
        value = listOf(
            MatchLocation("High Park", LatLng(0.0, 0.0)),
            MatchLocation("Columbia Icefield", LatLng(0.0, 0.0)),
            MatchLocation("Trinity Bellwoods Park", LatLng(0.0, 0.0))
        )
    }
    var hostLocationSuggestions: MutableLiveData<List<MatchLocation>> = _locationSuggestions
    var user = "NO-USER" // TODO: Fetch from user service
}
