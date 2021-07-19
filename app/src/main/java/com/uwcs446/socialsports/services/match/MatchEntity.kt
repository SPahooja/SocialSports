package com.uwcs446.socialsports.services.match

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.services.user.UserEntity
import java.time.LocalDateTime
import java.util.UUID

data class MatchEntity(
    val id: String = UUID.randomUUID().toString(),
    val sport: Sport = Sport.ANY,
    val title: String = "",
    val location: LocationEntity = LocationEntity(),
    val description: String = "",
    val time: String = LocalDateTime.now().toString(),
    val duration: Long = 0,
    val host: UserEntity = UserEntity(),
    val teamOne: List<String> = emptyList(),
    val teamTwo: List<String> = emptyList()
)

data class LocationEntity(
    val placeId: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val geohash: String = GeoFireUtils.getGeoHashForLocation(GeoLocation(lat, lng)),
)
