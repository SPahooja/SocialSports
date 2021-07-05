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
        date = date,
        time = time,
        duration = duration.toMinutes(),
        teamOne = teamOne.map { it.toEntity() },
        teamTwo = teamTwo.map { it.toEntity() }
    )

fun MatchEntity.toDomain() =
    Match(
        id = id,
        type = type,
        title = title,
        description = description,
        date = date,
        time = time,
        duration = Duration.ofMinutes(duration),
        host = host.toDomain(),
        teamOne = teamOne.map { it.toDomain() },
        teamTwo = teamTwo.map { it.toDomain() }
    )
