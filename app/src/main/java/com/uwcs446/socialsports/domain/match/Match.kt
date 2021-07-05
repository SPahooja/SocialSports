package com.uwcs446.socialsports.domain.match

import com.uwcs446.socialsports.domain.user.User
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

data class Match(
    val id: String,
    val type: MatchType,
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: LocalTime,
    val duration: Duration,
    val host: User,
    val teamOne: List<User>,
    val teamTwo: List<User>
) {
    fun teamSize() = this.type.teamSize

    fun maxPlayerCount() = this.type.teamSize * 2

    fun currentPlayerCount() = teamOne.size.plus(teamTwo.size)
}
