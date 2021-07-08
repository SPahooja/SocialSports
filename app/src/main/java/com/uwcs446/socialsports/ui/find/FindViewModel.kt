package com.uwcs446.socialsports.ui.find

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.ui.matchlist.MatchListUtils
import java.time.LocalDate

class FindViewModel : ViewModel() {

    // TODO: update matchList to hold data for all matches
    private val _matchList = MutableLiveData<List<Match>>().apply {
        this.value = MatchListUtils.genFakeMatchData(5)
    }

    val matchList: LiveData<List<Match>> = _matchList

    // TODO: filter match by date, remove filtering if time is null
    fun filterMatchByDate(time: LocalDate?) {}

    // TODO: filter match by time, remove filtering if both values are -1
    fun filterMatchByTime(hour: Int, minute: Int) {}

    // TODO: filter match by type, remove filtering if type is "ALL"
    fun filterMatchByType(type: String) {}
}
