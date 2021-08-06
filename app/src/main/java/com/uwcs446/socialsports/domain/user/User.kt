package com.uwcs446.socialsports.domain.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val rating: Float,
    val numRatings: Int,
) : Parcelable
