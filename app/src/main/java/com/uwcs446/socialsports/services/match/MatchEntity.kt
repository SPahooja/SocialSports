package com.uwcs446.socialsports.services.match

import com.uwcs446.socialsports.domain.match.MatchType
import com.uwcs446.socialsports.services.user.UserEntity
import java.time.Instant

data class MatchEntity(
    val id: String,
    val title: String,
    val type: MatchType,
    val description: String,
    val host: UserEntity,
    val time: Instant,
    val duration: Long, // in mins
    val players: List<List<UserEntity>>
)
