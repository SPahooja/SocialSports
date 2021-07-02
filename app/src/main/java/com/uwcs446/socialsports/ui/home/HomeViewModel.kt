package com.uwcs446.socialsports.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.ui.matchlist.MatchListUtils

class HomeViewModel : ViewModel() {

    //TODO: update matchList to hold the current user's matches
    private val _userMatchList = MutableLiveData<List<List<String>>>().apply {
        this.value = MatchListUtils.genFakeMatchData(2)
    }

    val userMatchList: LiveData<List<List<String>>> = _userMatchList
}
