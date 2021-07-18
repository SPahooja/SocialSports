package com.uwcs446.socialsports.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.user.CurrentUserRepository
import com.uwcs446.socialsports.ui.matchlist.MatchListUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val currentUserRepository: CurrentUserRepository
): ViewModel() {

    // TODO: update matchList to hold the current user's matches
    private val _userMatchList = MutableLiveData<List<Match>>().apply {
        this.value = MatchListUtils.genFakeMatchData(2)
    }

    val userMatchList: LiveData<List<Match>> = _userMatchList

    fun filterJoinedMatches() {
        println("calling filterJoinedMatches!")
        _userMatchList.value = MatchListUtils.genFakeMatchData(5).filter {
            isJoinedMatch(it)
        }
    }

    fun filterHostingMatches() {
        println("calling filterHostingMatches!")
        _userMatchList.value = MatchListUtils.genFakeMatchData(5).filter {
            isHostingMatch(it)
        }
    }

    fun filterPastMatches() {
        println("calling filterPastMatches!")
        _userMatchList.value = MatchListUtils.genFakeMatchData(5).filter {
            val isPartOfMatch = isJoinedMatch(it) || isHostingMatch(it)
            val isInPast = it.date < LocalDate.now()

            isPartOfMatch && isInPast
        }
    }

    private fun isJoinedMatch(match: Match): Boolean {
        val currentUser = currentUserRepository.getUser()

        val inTeamOne = currentUser in match.teamOne
        val inTeamTwo = currentUser in match.teamTwo

        return inTeamOne || inTeamTwo
    }

    private fun isHostingMatch(match: Match): Boolean {
        val currentUser = currentUserRepository.getUser()

        return match.host == currentUser
    }
}
