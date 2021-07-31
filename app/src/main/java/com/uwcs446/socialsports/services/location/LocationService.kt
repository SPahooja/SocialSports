package com.uwcs446.socialsports.services.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class LocationService @Inject constructor(
    @ApplicationContext private val activity: Context
) {
    suspend fun getPlace(placeId: String): Place {
        val placesClient = Places.createClient(activity)
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        return placesClient.fetchPlace(request).await().place
    }

    suspend fun getCurrentPlace(): Place? {
        val placesClient = Places.createClient(activity)
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        var curPlace: Place? = null
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val places = placesClient.findCurrentPlace(request).await().placeLikelihoods
            if (places.isNotEmpty()) {
                curPlace = places[0].place
            }
        }
        return placesClient.findCurrentPlace(request).await().placeLikelihoods[0].place
    }
}
