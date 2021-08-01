package com.uwcs446.socialsports.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.uwcs446.socialsports.domain.location.LocationRepository
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currentUserRepository: CurrentAuthUserRepository,
    private val matchRepository: MatchRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val currentUser = currentUserRepository.getUser()

    private val _matchPlaces = MutableLiveData<List<Pair<Match, Place>>>(emptyList())
    val matchPlaces: LiveData<List<Pair<Match, Place>>> = _matchPlaces

    private val _filterType = MutableLiveData(MatchFilter.JOINED)
    val filterType: LiveData<MatchFilter> = _filterType

    fun setFilterType(filter: MatchFilter) {
        filterMatches(filter)

        if (_filterType.value == filter) return
        _filterType.value = filter
    }

    private fun filterMatches(filter: MatchFilter) {
        if (currentUser == null) {
            _matchPlaces.value = _matchPlaces.value // no-op
            return
        }

        viewModelScope.launch {
            val fetchedMatches = when (filter) {
                // Instead of using uid of firebase user, fetch current user from our domain and use it's id
                MatchFilter.JOINED -> matchRepository.findJoinedByUser(currentUser.uid)
                MatchFilter.HOSTING -> matchRepository.findAllByHost(currentUser.uid)
                MatchFilter.PAST -> matchRepository.findPastWithUser(currentUser.uid)
            }
            val fetchedPlaces = fetchedMatches.map { match -> locationRepository.getPlace(match.location.placeId) }
            _matchPlaces.value = fetchedMatches.zip(fetchedPlaces)
        }
    }
}
