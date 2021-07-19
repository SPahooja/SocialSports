package com.uwcs446.socialsports.services

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LocationService @Inject constructor(
    @ActivityContext private val activity: Context
) {
    fun getPlacesClient(): PlacesClient {
        return Places.createClient(activity)
    }
}
