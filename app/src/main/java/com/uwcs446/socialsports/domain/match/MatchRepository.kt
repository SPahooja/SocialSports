package com.uwcs446.socialsports.domain.match

interface MatchRepository {

    fun findAllByUser(userId: String): List<Match>

    fun create(match: Match)

    fun edit(match: Match)

    fun delete(matchId: String)
}
