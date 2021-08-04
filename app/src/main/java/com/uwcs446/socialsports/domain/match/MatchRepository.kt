package com.uwcs446.socialsports.domain.match

import androidx.lifecycle.LiveData
import java.time.Instant

interface MatchRepository {

    val exploreMatches: LiveData<List<Match>>

    val matchesByHost: LiveData<Pair<String, List<Match>>>

    suspend fun fetchExploreMatches(sport: Sport, dateTime: Instant): List<Match>

    suspend fun findAllByHost(hostId: String): List<Match>

    suspend fun findJoinedByUser(userId: String): List<Match>

    suspend fun findPastWithUser(userId: String): List<Match>

    suspend fun fetchMatchById(matchId: String): Match?

    suspend fun joinMatch(matchId: String, userId: String, team: Int): Boolean

    suspend fun leaveMatch(matchId: String, userId: String, team: Int): Boolean

    fun create(match: Match)

    fun edit(match: Match)

    fun delete(matchId: String)
}
