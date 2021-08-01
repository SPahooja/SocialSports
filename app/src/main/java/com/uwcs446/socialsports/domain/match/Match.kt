package com.uwcs446.socialsports.domain.match

import android.os.Parcelable
import com.uwcs446.socialsports.domain.location.Location
import kotlinx.android.parcel.Parcelize
import java.time.Instant

@Parcelize
data class Match(
    val id: String,
    val sport: Sport,
    val title: String,
    val description: String,
    val location: Location,
    val startTime: Instant,
    val endTime: Instant,
    val hostId: String,
    val teamOne: List<String>,
    val teamTwo: List<String>,
    val blacklist: List<String>
) : Parcelable {
    fun teamSize() = this.sport.teamSize

    fun maxPlayerCount() = this.sport.teamSize * 2

    fun currentPlayerCount() = teamOne.size.plus(teamTwo.size)
}
