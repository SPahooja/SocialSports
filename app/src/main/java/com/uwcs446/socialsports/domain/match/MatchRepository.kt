package com.uwcs446.socialsports.domain.match

interface MatchRepository {

    fun findAllByUser(userId: String): List<Match>

    fun create(match: Match): Match

    fun edit(match: Match): Match

    fun delete(matchId: String)
}
