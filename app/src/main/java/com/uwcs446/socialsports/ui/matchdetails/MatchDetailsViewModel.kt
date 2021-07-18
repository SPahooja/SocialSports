package com.uwcs446.socialsports.ui.matchdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class MatchDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    matchRepository: MatchRepository
) : ViewModel() {

    private val matchId: String = savedStateHandle["matchId"] ?: throw IllegalArgumentException("Missing matchId")

    private val _match = MutableLiveData<Match>()

    val match: LiveData<Match> = _match

    init {
        viewModelScope.launch {
            _match.value = matchRepository.exploreMatches.value?.find { match -> match.id == matchId }
        }
    }
}
