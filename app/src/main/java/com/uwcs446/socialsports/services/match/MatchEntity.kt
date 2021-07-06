package com.uwcs446.socialsports.services.match

import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.services.user.UserEntity
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class MatchEntity(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val sport: Sport = Sport.NOT_SET,
    val description: String = "",
    val host: UserEntity = UserEntity(),
    val date: Instant = LocalDate.now().,
    val time: Long = LocalTime.now(),
    val duration: Long = 0,
    val teamOne: List<UserEntity> = emptyList(),
    val teamTwo: List<UserEntity> = emptyList()
)
