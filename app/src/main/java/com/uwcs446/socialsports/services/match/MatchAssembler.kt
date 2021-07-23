package com.uwcs446.socialsports.services.match

import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchLocation
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
        location = LocationEntity(placeId = location.placeId, lat = location.latLng.latitude, lng = location.latLng.longitude),
        duration = duration.toMinutes(),
        teamOne = teamOne,
        teamTwo = teamTwo
    )

fun Collection<MatchEntity>.toDomain(): List<Match> {
    return this.map { it.toDomain() }
}

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
        location = MatchLocation(
            location.placeId,
            LatLng(
                location.lat,
                location.lng
            )
        ),
        teamOne = teamOne,
        teamTwo = teamTwo
    )
