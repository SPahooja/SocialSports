package com.uwcs446.socialsports.domain.match

import androidx.lifecycle.LiveData
import com.uwcs446.socialsports.domain.user.User

interface MatchRepository {

    val myMatches: LiveData<List<Match>>

    val exploreMatches: LiveData<List<Match>>

    val matchesByHost: LiveData<Pair<String, List<Match>>>

    fun fetchMyMatches(user: User)

    fun fetchExploreMatches(sport: Sport)

    fun findAllByHost(hostId: String)

    fun create(match: Match)

    fun edit(match: Match)

    fun delete(matchId: String)
}
