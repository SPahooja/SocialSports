package com.uwcs446.socialsports.domain.location

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HostLocation(
    val title: String,
    val address: String,
    val distance: String
) : Parcelable
