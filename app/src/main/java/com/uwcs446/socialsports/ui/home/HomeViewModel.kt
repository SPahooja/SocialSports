package com.uwcs446.socialsports.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.ui.matchlist.MatchListUtils

class HomeViewModel : ViewModel() {

    // TODO: update matchList to hold the current user's matches
    private val _userMatchList = MutableLiveData<List<Match>>().apply {
        this.value = MatchListUtils.genFakeMatchData(1)
    }

    val userMatchList: LiveData<List<Match>> = _userMatchList
}
