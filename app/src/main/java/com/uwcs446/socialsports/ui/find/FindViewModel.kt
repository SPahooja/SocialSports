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
    private val locationService: LocationService
) : ViewModel() {

    private val _matchesBySport = MutableLiveData<List<Match>>(emptyList())
    private val _matchesByDistance = MutableLiveData<List<Match>>(emptyList())
    // TODO: filter by date and time
    private val _matchesByDate = MutableLiveData<List<Match>>(emptyList())
    private val _matchesByTime = MutableLiveData<List<Match>>(emptyList())

    val _matchPlaces = MutableLiveData<List<Pair<Match, Place>>>(emptyList())
    val matchPlaces: LiveData<List<Pair<Match, Place>>> = _matchPlaces

    init {
        viewModelScope.launch {
            _matchesBySport.value = matchRepository.fetchExploreMatches(Sport.ANY)
            updateMatchList()
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
        _matchesBySport.value = matchRepository.fetchExploreMatches(sport)
        updateMatchList()
    }

    /**
     * Filters matches by distance.
     */
    suspend fun filterMatchByDistance(distance: Int) {
        val curLatLng = locationService.getCurrentPlace()?.latLng ?: null
        _matchesByDistance.value = matchRepository.fetchWithinDistance(curLatLng, distance)
        updateMatchList()
    }

    /**
     * Remove distance filter.
     */
    suspend fun removeDistanceFilter() {
        _matchesByDistance.value = matchRepository.fetchWithinDistance(null, null)
        updateMatchList()
    }

    private suspend fun updateMatchList() {
        val matchesBySport: List<Match> = _matchesBySport.value ?: emptyList()
        val matchesByDistance: List<Match> = _matchesByDistance.value ?: emptyList()
        val matchesByDate: List<Match> = _matchesByDate.value ?: emptyList()
        val matchesByTime: List<Match> = _matchesByTime.value ?: emptyList()

        val idsBySport = matchesBySport.map { m -> m.id }
        // TODO: uncomment after filter by date and time implemented
        // val idsBySportDate = matchesByDate.filter { m -> idsBySport.contains(m.id) }.map { m -> m.id }
        // val idsBySportDateTime = matchesByTime.filter { m -> idsBySportDate.contains(m.id) }.map { m -> m.id }
        val fetchedMatches = matchesByDistance.filter { m -> idsBySport.contains(m.id) }
        val fetchedPlaces = fetchedMatches.map { match -> locationService.getPlace(match.location.placeId) }
        _matchPlaces.value = fetchedMatches.zip(fetchedPlaces)
    }
}
