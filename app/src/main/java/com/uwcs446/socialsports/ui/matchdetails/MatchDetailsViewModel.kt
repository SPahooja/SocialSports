package com.uwcs446.socialsports.ui.matchdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import com.uwcs446.socialsports.domain.user.User
import com.uwcs446.socialsports.domain.user.UserRepository
import com.uwcs446.socialsports.services.location.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchDetailsViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    private val currentUserRepository: CurrentAuthUserRepository,
    private val userRepository: UserRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _match = MutableLiveData<Match>()
    val match: LiveData<Match> = _match

    private val _host = MutableLiveData<User>()
    val host: LiveData<User> = _host

    private val _teamOne = MutableLiveData<List<User>>(emptyList())
    val teamOne: LiveData<List<User>> = _teamOne

    private val _teamTwo = MutableLiveData<List<User>>(emptyList())
    val teamTwo: LiveData<List<User>> = _teamTwo

    private val _ready = MutableLiveData<Boolean>(false)
    val ready: LiveData<Boolean> = _ready

    private val _matchPlace = MutableLiveData<Place>()
    val matchPlace: LiveData<Place> = _matchPlace

    suspend fun fetchMatch(matchId: String) {
        _ready.value = false

        val fetchedMatch = matchRepository.fetchMatchById(matchId)
        if (fetchedMatch != null) {
            val fetchedHost = userRepository.findById(fetchedMatch.hostId)
            val fetchedTeamOne = userRepository.findByIds(fetchedMatch.teamOne)
            val fetchedTeamTwo = userRepository.findByIds(fetchedMatch.teamTwo)
            val fetchedMatchedPlace = locationService.getPlace(fetchedMatch.location.placeId)

            _match.value = fetchedMatch
            _host.value = fetchedHost!!
            _teamOne.value = fetchedTeamOne
            _teamTwo.value = fetchedTeamTwo
            _matchPlace.value = fetchedMatchedPlace

            _ready.value = true
        }
    }

    fun isInTeam(team: Int): Boolean {
        val currentUser = currentUserRepository.getUser() ?: return false
        val match = _match.value ?: return false
        return when (team) {
            1 -> match.teamOne.contains(currentUser.uid)
            2 -> match.teamTwo.contains(currentUser.uid)
            else -> false
        }
    }

    fun isInMatch(): Boolean {
        return isInTeam(1) || isInTeam(2)
    }

    fun joinMatch(team: Int) {
        val currentUser = currentUserRepository.getUser() ?: return
        val match = _match.value ?: return
        viewModelScope.launch {
            matchRepository.joinMatch(match.id, currentUser.uid, team)
            fetchMatch(match.id)
        }
    }

    fun leaveMatch(team: Int) {
        val currentUser = currentUserRepository.getUser() ?: return
        val match = _match.value ?: return
        viewModelScope.launch {
            matchRepository.leaveMatch(match.id, currentUser.uid, team)
            fetchMatch(match.id)
        }
    }
}
