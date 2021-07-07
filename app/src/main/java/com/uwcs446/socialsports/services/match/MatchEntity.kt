package com.uwcs446.socialsports.services.match

import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.services.user.UserEntity
import java.time.LocalDateTime
import java.util.UUID

data class MatchEntity(
    val id: String = UUID.randomUUID().toString(),
    val sport: Sport = Sport.NOT_SET,
    val title: String = "",
    val description: String = "",
//    val date: String = LocalDate.now().toString(),
    val time: String = LocalDateTime.now().toString(),
    val duration: Long = 0,
    val host: UserEntity = UserEntity(),
    val teamOne: List<UserEntity> = emptyList(),
    val teamTwo: List<UserEntity> = emptyList()
)
