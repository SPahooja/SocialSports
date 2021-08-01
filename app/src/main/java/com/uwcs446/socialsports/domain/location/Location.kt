package com.uwcs446.socialsports.domain.location

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    val placeId: String,
    val latLng: LatLng
) : Parcelable
