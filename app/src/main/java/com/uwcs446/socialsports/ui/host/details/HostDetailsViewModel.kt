package com.uwcs446.socialsports.ui.host.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.location.HostLocation
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.user.CurrentUserRepository
import com.uwcs446.socialsports.domain.user.User
import com.uwcs446.socialsports.services.match.MatchRepository
import com.uwcs446.socialsports.services.match.MatchType
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
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

    // TODO: Get match model from matchRepository by matchId
    private val editMatchId = state.get<String>("matchId")
    private var editMatch = editMatchId?.let {
        Match(
            id = "id_of_existing_match",
            type = MatchType.OTHER,
            title = "title_of_existing_match",
            description = "discription_of_existing_match",
            time = LocalDateTime.now(),
            capacity = 6,
            host = User("testUser"),
            participants = setOf(User("testUser2"))
        )
    }

    // Initialize with existing match info if applicable
    var locationTitle = selectedHostLocation?.title ?: ""
    var locationAddress = selectedHostLocation?.address ?: ""
    var matchTitle = editMatch?.title ?: ""
    var sportType = editMatch?.type ?: ""
    var matchDate = editMatch?.time?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) ?: ""
    var matchTime = editMatch?.time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""
    var matchCapacity = editMatch?.capacity ?: ""
    var matchDescription = editMatch?.description ?: ""

    var user = "NO-USER" // TODO: Fetch from user service

    fun onSaveClick() {
        val dateTime = LocalDateTime.parse("${matchDate}T${matchTime}")
        if (editMatchId != null) {
            val updatedMatch = editMatch?.copy(
                title = matchTitle,
                type = MatchType.valueOf(sportType.toString()),
                time = dateTime,
                capacity = matchCapacity.toString().toInt(),
                description = matchDescription
            )
            // TODO[BACKEND]: Update match information
        } else {
            // val newMatch = Match()
            // TODO[BACKEND]: Save new match information
        }
    }

}