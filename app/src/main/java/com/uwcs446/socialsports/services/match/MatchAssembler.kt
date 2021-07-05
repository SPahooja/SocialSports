package com.uwcs446.socialsports.services.match

import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.services.user.toDomain
import com.uwcs446.socialsports.services.user.toEntity
import java.time.Duration

fun Match.toEntity() =
    MatchEntity(
        id = id,
        title = title,
        type = type,
        description = description,
        host = host.toEntity(),
        time = time,
        duration = duration.toMinutes(),
        players = players.map { it.map { user -> user.toEntity() } }
    )

fun MatchEntity.toDomain() =
    Match(
        id = id,
        title = title,
        type = type,
        description = description,
        host = host.toDomain(),
        time = time,
        duration = Duration.ofMinutes(duration),
        players = players.map { it.map { user -> user.toDomain() } }
    )
