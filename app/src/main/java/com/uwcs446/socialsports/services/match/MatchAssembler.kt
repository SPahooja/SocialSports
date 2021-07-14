package com.uwcs446.socialsports.services.match

import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.match.Location
import com.uwcs446.socialsports.domain.match.Match
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
        location = LocationEntity(lat = location.latLng.latitude, lng = location.latLng.longitude),
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
        date = LocalDate.from(LocalDateTime.parse(time)),
        time = LocalTime.from(LocalDateTime.parse(time)),
        duration = Duration.ofMinutes(duration),
        host = host.toDomain(),
        location = Location(
            LatLng(
                location.lat,
                location.lng
            )
        ),
        teamOne = teamOne.map { it.toDomain() },
        teamTwo = teamTwo.map { it.toDomain() }
    )
