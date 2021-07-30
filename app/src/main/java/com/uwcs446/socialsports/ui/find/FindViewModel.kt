package com.uwcs446.socialsports.ui.find

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.services.location.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FindViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    private val locationService: LocationService,
) : ViewModel() {

    val _matchPlaces = MutableLiveData<List<Pair<Match, Place>>>(emptyList())
    val matchPlaces: LiveData<List<Pair<Match, Place>>> = _matchPlaces

    init {
        viewModelScope.launch {
            filterMatchBySport(Sport.ANY)
        }
    }

    // TODO: filter match by date, remove filtering if time is null
    fun filterMatchByDate(time: LocalDate?) {}

    // TODO: filter match by time, remove filtering if both values are -1
    fun filterMatchByTime(hour: Int, minute: Int) {}

    /**
     * Filters matches by sport.
     */
    suspend fun filterMatchBySport(sport: Sport) {
        val fetchedMatches = matchRepository.fetchExploreMatches(sport)
        val fetchedPlaces = fetchedMatches.map { match -> locationService.getPlace(match.location.placeId) }

        _matchPlaces.value = fetchedMatches.zip(fetchedPlaces)
    }
}
