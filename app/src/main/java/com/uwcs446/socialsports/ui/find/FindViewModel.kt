package com.uwcs446.socialsports.ui.find

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FindViewModel @Inject constructor(
    val matchRepository: MatchRepository,
) : ViewModel() {

    val matchList: LiveData<List<Match>> = matchRepository.exploreMatches

    init {
        matchRepository.fetchExploreMatches(Sport.ANY)
    }

    // TODO: filter match by date, remove filtering if time is null
    fun filterMatchByDate(time: LocalDate?) {}

    // TODO: filter match by time, remove filtering if both values are -1
    fun filterMatchByTime(hour: Int, minute: Int) {}

    /**
     * Filters matches by sport. Shows all matches if sport is `null`.
     */
    fun filterMatchBySport(sport: Sport) {
        matchRepository.fetchExploreMatches(sport)
    }
}
