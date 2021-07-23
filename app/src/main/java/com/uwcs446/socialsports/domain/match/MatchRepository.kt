package com.uwcs446.socialsports.domain.match

import androidx.lifecycle.LiveData

interface MatchRepository {

    val myGames: LiveData<List<Match>>

    suspend fun fetchExploreMatches(sport: Sport): List<Match>?

    suspend fun findAllByHost(hostId: String): List<Match>?

    suspend fun findJoinedByUser(userId: String): List<Match>?

    suspend fun findPastWithUser(userId: String): List<Match>?

    suspend fun join(matchId: String, userId: String, team: Int)

    suspend fun findByIds(ids: List<String>): List<Match?>

    fun create(match: Match)

    fun edit(match: Match)

    fun delete(matchId: String)
}
