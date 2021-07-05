package com.uwcs446.socialsports.domain.match

import com.uwcs446.socialsports.domain.user.User
import java.time.Duration
import java.time.Instant

data class Match(
    val id: String,
    val type: MatchType,
    val title: String,
    val description: String,
    val time: Instant,
    val duration: Duration,
    val host: User,
    val players: List<List<User>>,
) {
    fun teamCount() = this.type.teamCount

    fun teamSize() = this.type.teamSize

    fun playersByTeam(team: Int): List<User> {
        return if (team > teamCount()) emptyList()
        else players[team - 1]
    }
}
