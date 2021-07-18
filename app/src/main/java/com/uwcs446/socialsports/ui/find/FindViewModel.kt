package com.uwcs446.socialsports.ui.find

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FindViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
) : ViewModel() {

    val _matches = MutableLiveData<List<Match>>(emptyList())

    val matches: LiveData<List<Match>> = _matches

    init {
        viewModelScope.launch {
            _matches.value = matchRepository.fetchExploreMatches(Sport.ANY)
        }
    }

    // TODO: filter match by date, remove filtering if time is null
    fun filterMatchByDate(time: LocalDate?) {}

    // TODO: filter match by time, remove filtering if both values are -1
    fun filterMatchByTime(hour: Int, minute: Int) {}

    /**
     * Filters matches by sport.
     */
    fun filterMatchBySport(sport: Sport) {
        _matches.value = _matches.value?.filter { match -> sport == Sport.ANY || match.sport == sport }
    }
}
