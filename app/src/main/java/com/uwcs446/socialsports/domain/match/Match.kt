package com.uwcs446.socialsports.domain.match

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.user.User
import kotlinx.android.parcel.Parcelize
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

data class Match(
    val id: String,
    val sport: Sport,
    val title: String,
    val description: String,
    val location: MatchLocation,
    val date: LocalDate,
    val time: LocalTime,
    val duration: Duration,
    val host: User,
    val teamOne: List<String>,
    val teamTwo: List<String>
) {
    fun teamSize() = this.sport.teamSize

    fun maxPlayerCount() = this.sport.teamSize * 2

    fun currentPlayerCount() = teamOne.size.plus(teamTwo.size)
}

@Parcelize
data class MatchLocation(
    val placeId: String,
    val latLng: LatLng
) : Parcelable
