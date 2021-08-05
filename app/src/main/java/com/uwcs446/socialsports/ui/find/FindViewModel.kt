package com.uwcs446.socialsports.ui.find

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.uwcs446.socialsports.domain.datetimepicker.DateTimePicker
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.services.location.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FindViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    private val locationService: LocationService
) : ViewModel() {
    val filterDate: MutableLiveData<String> = MutableLiveData<String>("")
    val filterTime: MutableLiveData<String> = MutableLiveData<String>("")

    private val _matchesBySport = MutableLiveData<List<Match>>(emptyList())
    private val _matchesByDistance = MutableLiveData<List<Match>>(emptyList())
    private val _matchesByDateTime = MutableLiveData<List<Match>>(emptyList())

    val _matchPlaces = MutableLiveData<List<Pair<Match, Place>>>(emptyList())
    val matchPlaces: LiveData<List<Pair<Match, Place>>> = _matchPlaces

    init {
        viewModelScope.launch {
            _matchesBySport.value = matchRepository.fetchExploreMatches(Sport.ANY)
            updateMatchList()
        }
    }

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
        val curLatLng = locationService.getCurrentPlace()?.latLng
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

    suspend fun filterMatchByDateTime() {
        val date = filterDate.value ?: ""
        val time = filterTime.value ?: ""
        val dateTime = getFilterDateTime(date, time)

        _matchesByDateTime.value = matchRepository.fetchAfterDateTime(dateTime)
        updateMatchList()
    }

    private fun getFilterDateTime(date: String, time: String): Instant {
        val currentDate = DateTimePicker()
            .getDateFormatter()
            .withZone(ZoneId.systemDefault())
            .format(Instant.now())

        var currentTime = DateTimePicker()
            .getTimeFormatter()
            .withZone(ZoneId.systemDefault())
            .format(Instant.now())

        val formattedDate = if (date.isBlank()) currentDate else date
        var formattedTime = time
        if (time.isBlank() && formattedDate == currentDate) {
            formattedTime = currentTime
        } else if (time.isBlank()) {
            formattedTime = "00:00"
        }

        return LocalDateTime.parse(
            "$formattedDate $formattedTime",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        )
            .atZone(ZoneId.systemDefault())
            .toInstant()
    }

    private suspend fun updateMatchList() {
        val matchesBySport: List<Match> = _matchesBySport.value ?: emptyList()
        val matchesByDistance: List<Match> = _matchesByDistance.value ?: emptyList()
        val matchesByDateTime: List<Match> = _matchesByDateTime.value ?: emptyList()

        val idsBySport = matchesBySport.map { m -> m.id }
        val idsBySportDateTime = matchesByDateTime.filter { m -> idsBySport.contains(m.id) }.map { m -> m.id }
        val fetchedMatches = matchesByDistance.filter { m -> idsBySportDateTime.contains(m.id) }
        val fetchedPlaces = fetchedMatches.map { match -> locationService.getPlace(match.location.placeId) }
        _matchPlaces.value = fetchedMatches.zip(fetchedPlaces)
    }
}
