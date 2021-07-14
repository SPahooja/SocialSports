package com.uwcs446.socialsports.services

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.Locale
import javax.inject.Inject

@ActivityScoped
class LocationService @Inject constructor(
    @ActivityContext private val activity: Context
) {

    private val DEFAULT_ADDRESS = Address(Locale.getDefault())

    fun getAddress(latLng: LatLng): Address {

        val geocoder = Geocoder(activity, Locale.getDefault())

        return geocoder
            .getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            ).firstOrNull() ?: DEFAULT_ADDRESS
    }
}
