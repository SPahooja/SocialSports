package com.uwcs446.socialsports.ui.host.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.location.HostLocation
import com.uwcs446.socialsports.domain.match.Location
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.domain.user.CurrentUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HostDetailsViewModel @Inject constructor(
    private val currentUserRepository: CurrentUserRepository,
    private val matchRepository: MatchRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    // To edit an existing match, pass matchId to HostDetailsFragment as arguments and
    // use SavedStateHandle to handle fragment arguments to fill the form with existing match info
    private val selectedHostLocation = state.get<HostLocation>("hostLocation")
    private val editMatchId =
        state.get<String>("matchId") // TODO: Get match model from matchRepository by matchId

    // mock match data
    private var editMatch = editMatchId?.let {
        Match(
            id = "id_of_existing_match",
            sport = Sport.SOCCER,
            title = "title_of_existing_match",
            description = "discription_of_existing_match",
            date = LocalDate.now(),
            time = LocalTime.now(),
            duration = Duration.parse("PT8H"),
            hostId = UUID.randomUUID().toString(),
            location = Location(LatLng(0.0, 0.0)),
            teamOne = listOf(UUID.randomUUID().toString()),
            teamTwo = listOf(UUID.randomUUID().toString()),
            // TODO: location
        )
    }

    // Initialize with existing match info if applicable
    var locationTitle = selectedHostLocation?.title ?: ""
    var locationAddress = selectedHostLocation?.address ?: ""
    var matchTitle = editMatch?.title ?: ""
    var sportType = editMatch?.sport ?: ""
    var matchDate = editMatch?.date?.toString() ?: ""
    var matchTime = editMatch?.time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""
    var matchDurationHour = editMatch?.duration?.toHours() ?: ""
    var matchDurationMinute = editMatch?.duration?.toMinutes()?.rem(60) ?: ""

    //    var matchCapacity = editMatch?.capacity ?: ""
    var matchDescription = editMatch?.description ?: ""

    var user = "NO-USER" // TODO: Fetch from user service

    fun onSaveClick() {
        val durationHour = Duration.ofHours(
            if (matchDurationHour == "") 0 else matchDurationHour.toString().toLong()
        )
        val durationMin = Duration.ofMinutes(
            if (matchDurationMinute == "") 0 else matchDurationMinute.toString().toLong()
        )
        if (editMatchId != null) {
            val updatedMatch = editMatch?.copy(
                title = matchTitle,
                sport = Sport.valueOf(sportType.toString()),
                date = LocalDate.parse(matchDate),
                time = LocalTime.parse(matchTime),
                duration = durationHour + durationMin,
//                capacity = matchCapacity.toString().toInt(),
                description = matchDescription
            )
            // TODO[BACKEND]: Update match information
        } else {
            // val newMatch = Match()
            // TODO[BACKEND]: Save new match information
        }
    }
}
