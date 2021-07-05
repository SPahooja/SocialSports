package com.uwcs446.socialsports.ui.host.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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
            HostLocation("Trinity Bellwoods Park", "790 Queen St W, Toronto, ON, M6J 1G3", "4.2 km"),
            HostLocation("High Park", "1873 Bloor St W, Toronto, ON, M6R 2Z3", "3.6 km"),
            HostLocation("Trinity Bellwoods Park", "790 Queen St W, Toronto, ON, M6J 1G3", "4.2 km")
        )
    }

    private val _locationSearchResults = MutableLiveData<List<HostLocation>>()

    var hostLocationSuggestions : MutableLiveData<List<HostLocation>> = _locationSuggestions
    var hostLocationSearchResults: LiveData<List<HostLocation>> = _locationSearchResults
    var user = "NO-USER" // TODO: Fetch from user service

    fun getLocationSearchResults(query: String) {
        // TODO[BACKEND]: get location search result
        _locationSearchResults.apply {
            value = listOf(
                HostLocation("High Park", "1873 Bloor St W, Toronto, ON, M6R 2Z3", "3.6 km"),
                HostLocation("Trinity Bellwoods Park", "790 Queen St W, Toronto, ON, M6J 1G3", "4.2 km"),
                HostLocation("High Park", "1873 Bloor St W, Toronto, ON, M6R 2Z3", "3.6 km"),
                HostLocation("Trinity Bellwoods Park", "790 Queen St W, Toronto, ON, M6J 1G3", "4.2 km")
            )
        }
    }
}



