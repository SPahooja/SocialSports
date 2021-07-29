package com.uwcs446.socialsports.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    private val currentUser = currentUserRepository.getUser()

    private val _matches = MutableLiveData<List<Match>>(emptyList())
    val matches: LiveData<List<Match>> = _matches

    init {
        if (currentUser != null) {
            viewModelScope.launch {
                _matches.value = matchRepository.findJoinedByUser(currentUser.uid)
            }
        }
    }

    fun filterMatches(filter: MatchFilter) {
        if (currentUser == null) {
            _matches.value = matches.value // no-op
            return
        }

        viewModelScope.launch {
            _matches.value = when (filter) {
                // Instead of using uid of firebase user, fetch current user from our domain and use it's id
                MatchFilter.JOINED -> matchRepository.findJoinedByUser(currentUser.uid)
                MatchFilter.HOSTING -> matchRepository.findAllByHost(currentUser.uid)
                MatchFilter.PAST -> matchRepository.findPastWithUser(currentUser.uid)
            }
        }
    }
}
