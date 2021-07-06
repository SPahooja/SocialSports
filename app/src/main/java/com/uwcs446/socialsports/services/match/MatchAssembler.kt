package com.uwcs446.socialsports.services.match

import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.services.user.toDomain
import com.uwcs446.socialsports.services.user.toEntity
import java.time.Duration

fun Collection<Match>.toEntity() = this.map { it.toEntity() }

fun Match.toEntity() =
    MatchEntity(
        id = id,
        title = title,
        sport = sport,
        description = description,
        host = host.toEntity(),
        date = date,
        time = time,
        duration = duration.toMinutes(),
        teamOne = teamOne.map { it.toEntity() },
        teamTwo = teamTwo.map { it.toEntity() }
    )

fun Collection<MatchEntity>.toDomain() = this.map { it.toDomain() }

fun MatchEntity.toDomain() =
    Match(
        id = id,
        sport = sport,
        title = title,
        description = description,
        date = date,
        time = time,
        duration = Duration.ofMinutes(duration),
        host = host.toDomain(),
        teamOne = teamOne.map { it.toDomain() },
        teamTwo = teamTwo.map { it.toDomain() }
    )
