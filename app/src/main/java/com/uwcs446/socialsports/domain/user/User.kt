package com.uwcs446.socialsports.domain.user

import android.os.Parcelable
import com.uwcs446.socialsports.domain.match.Match
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String,
    val matches: UserMatches
) : Parcelable

data class UserMatches(
    val player: List<String> = emptyList(),
    val host: List<String> = emptyList()
)

data class UserMatchDetails(
    val player: List<Match>,
    val host: List<Match>
)