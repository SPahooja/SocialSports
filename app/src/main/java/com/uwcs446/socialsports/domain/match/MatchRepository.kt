package com.uwcs446.socialsports.domain.match

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng

interface MatchRepository {

    val exploreMatches: LiveData<List<Match>>

    val matchesByHost: LiveData<Pair<String, List<Match>>>

    suspend fun fetchExploreMatches(sport: Sport): List<Match>

    suspend fun fetchWithinDistance(curLatLng: LatLng?, distance: Int?): List<Match>

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
