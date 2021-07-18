package com.uwcs446.socialsports.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.user.CurrentUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val matchRepository: MatchRepository,
    val currentUserRepository: CurrentUserRepository
) : ViewModel() {

    val matchList: LiveData<List<Match>> = matchRepository.myMatches

    init {
        val user = currentUserRepository.user.value
        if (user != null) {
            matchRepository.fetchMyMatches(user)
        }
    }
}
