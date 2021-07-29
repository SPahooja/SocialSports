package com.uwcs446.socialsports.services.match

import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchLocation
import java.time.Instant

fun Collection<Match>.toEntity() = this.map { it.toEntity() }

fun Match.toEntity() =
    MatchEntity(
        id = id,
        title = title,
        sport = sport,
        description = description,
        hostId = hostId,
        startTime = startTime.toEpochMilli(),
        endTime = startTime.toEpochMilli(),
        location = LocationEntity(
            placeId = location.placeId,
            lat = location.latLng.latitude,
            lng = location.latLng.longitude
        ),
        teamOne = teamOne,
        teamTwo = teamTwo,
        blacklist = emptyList()
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
        startTime = Instant.ofEpochMilli(startTime),
        endTime = Instant.ofEpochMilli(startTime),
        location = MatchLocation(
            location.placeId,
            LatLng(
                location.lat,
                location.lng
            )
        ),
        hostId = hostId,
        teamOne = teamOne,
        teamTwo = teamTwo,
        blacklist = emptyList()
    )
