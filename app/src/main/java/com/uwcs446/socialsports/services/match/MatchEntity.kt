package com.uwcs446.socialsports.services.match

import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.services.user.UserEntity
import java.time.LocalDate
import java.time.LocalTime

data class MatchEntity(
    val id: String,
    val title: String,
    val sport: Sport,
    val description: String,
    val host: UserEntity,
    val date: LocalDate,
    val time: LocalTime,
    val duration: Long,
    val teamOne: List<UserEntity>,
    val teamTwo: List<UserEntity>
)
