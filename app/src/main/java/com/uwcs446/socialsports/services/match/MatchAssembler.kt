package com.uwcs446.socialsports.services.match

import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.user.User
import com.uwcs446.socialsports.services.user.UserEntity
import com.uwcs446.socialsports.services.user.toDomain
import com.uwcs446.socialsports.services.user.toEntity
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun Collection<Match>.toEntity() = this.map { it.toEntity() }

fun Match.toEntity() =
    MatchEntity(
        id = id,
        title = title,
        sport = sport,
        description = description,
        host = host.toEntity(),
        time = time.atDate(date).toString(),
        location = location,
        duration = duration.toMinutes(),
        teamOne = teamOne.map { it.id },
        teamTwo = teamTwo.map { it.id }
    )

fun Collection<MatchEntity>.toDomain(users: List<UserEntity>): List<Match> {
    val domainUsers = users.toDomain()
    return this.map { it.toDomain(domainUsers) }
}

fun MatchEntity.toDomain(users: List<User>) =
    Match(
        id = id,
        sport = sport,
        title = title,
        description = description,
        date = LocalDate.from(LocalDateTime.parse(time)),
        time = LocalTime.from(LocalDateTime.parse(time)),
        duration = Duration.ofMinutes(duration),
        host = host.toDomain(),
        location = location,
        teamOne = users.filter { teamOne.contains(it.id) },
        teamTwo = users.filter { teamTwo.contains(it.id) }
    )
