package com.uwcs446.socialsports.ui.matchrating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import com.uwcs446.socialsports.domain.user.User
import com.uwcs446.socialsports.domain.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchRatingViewModel @Inject constructor (
    private val matchRepository: MatchRepository,
    private val userRepository: UserRepository,
    private val currentUserRepository: CurrentAuthUserRepository
) : ViewModel() {

    private val currentUser = currentUserRepository.getUser()

    private val _players = MutableLiveData<List<User>>(emptyList())
    val players: LiveData<List<User>> = _players

    private val _ready = MutableLiveData(false)
    val ready: LiveData<Boolean> = _ready

    suspend fun fetchMatchPlayers(matchId: String) {
        _ready.value = false

        val fetchedMatch = matchRepository.fetchMatchById(matchId)
        if (fetchedMatch != null) {
            val fetchedTeamOne = userRepository.findByIds(fetchedMatch.teamOne)

            val fetchedTeamTwo = userRepository.findByIds(fetchedMatch.teamTwo)

            _players.value = (fetchedTeamOne + fetchedTeamTwo).filter {
                it.id != currentUser?.uid ?: false
            }
            _ready.value = true
        }
    }

    // should we add suspend here?
    fun ratePlayers(playerRatings: MutableList<Float>) {
        viewModelScope.launch {
            playerRatings.forEachIndexed { index, rating ->
                if (rating != 0F) {
                    val user = _players.value?.get(index)!!
                    val userUpdated = User(user.id, user.name, ((user.rating * user.numRatings) + rating) / (user.numRatings + 1), user.numRatings + 1)
                    userRepository.upsert(userUpdated)
                }
            }
        }
    }
}
