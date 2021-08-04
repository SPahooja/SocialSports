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
    private val locationService: LocationService,
) : ViewModel() {

    val _matchPlaces = MutableLiveData<List<Pair<Match, Place>>>(emptyList())
    val matchPlaces: LiveData<List<Pair<Match, Place>>> = _matchPlaces

    val filterDate: MutableLiveData<String> = MutableLiveData<String>("")
    val filterTime: MutableLiveData<String> = MutableLiveData<String>("")
    val filterType: MutableLiveData<Sport> = MutableLiveData<Sport>(Sport.ANY)

    init {
        viewModelScope.launch {
            filterType.value = Sport.ANY
        }
    }

    // combine the date and time for filtering
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

    /**
     * Filters matches by sport.
     */
    suspend fun filterMatch() {
        val date = filterDate.value ?: ""
        val time = filterTime.value ?: ""
        val type = filterType.value ?: Sport.ANY
        val dateTime = getFilterDateTime(date, time)

        val fetchedMatches = matchRepository.fetchExploreMatches(type, dateTime)
        val fetchedPlaces = fetchedMatches.map { match -> locationService.getPlace(match.location.placeId) }

        _matchPlaces.value = fetchedMatches.zip(fetchedPlaces)
    }
}
