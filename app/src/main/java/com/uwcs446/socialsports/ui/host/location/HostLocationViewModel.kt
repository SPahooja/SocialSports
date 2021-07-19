package com.uwcs446.socialsports.ui.host.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.location.HostLocation
import com.uwcs446.socialsports.domain.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HostLocationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _locationSuggestions = MutableLiveData<List<HostLocation>>().apply {
        // TODO[BACKEND]: get location suggestions for current user
        value = listOf(
            HostLocation("High Park", "1873 Bloor St W, Toronto, ON, M6R 2Z3", "3.6 km"),
            HostLocation("High Park", "1873 Bloor St W, Toronto, ON, M6R 2Z3", "3.6 km"),
            HostLocation("Trinity Bellwoods Park", "790 Queen St W, Toronto, ON, M6J 1G3", "4.2 km")
        )
    }
    var hostLocationSuggestions: MutableLiveData<List<HostLocation>> = _locationSuggestions
    var user = "NO-USER" // TODO: Fetch from user service
}
