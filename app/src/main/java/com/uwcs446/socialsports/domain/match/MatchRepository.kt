package com.uwcs446.socialsports.domain.match

import androidx.lifecycle.LiveData

interface MatchRepository {

    val exploreMatches: LiveData<List<Match>>

    val matchesByHost: LiveData<Pair<String, List<Match>>>

    fun fetchExploreMatches()

    fun findAllByHost(hostId: String)

    fun create(match: Match)

    fun edit(match: Match)

    fun delete(matchId: String)
}
