package com.uwcs446.socialsports.domain.match

import androidx.lifecycle.LiveData

interface MatchRepository {

    val exploreMatches: LiveData<List<Match>>

    val matchesByHost: LiveData<Pair<String, List<Match>>>

    suspend fun fetchExploreMatches(sport: Sport): List<Match>?

    fun findAllByHost(hostId: String)

    fun create(match: Match)

    fun edit(match: Match)

    fun delete(matchId: String)
}
