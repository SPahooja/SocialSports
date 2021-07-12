package com.uwcs446.socialsports.ui.matchdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.ui.matchlist.MatchListUtils

class MatchDetailsViewModel : ViewModel() {

    private var _match = MutableLiveData<Match>().apply {
        this.value = null
    }

    val match: LiveData<Match> = _match

    fun findMatchById(id: String) {
        // Placeholder find the match that matches the passed in ID
        val matches = MatchListUtils.genFakeMatchData(id.toInt())
        _match.value = matches.find { match -> match.id == id }
    }

}
