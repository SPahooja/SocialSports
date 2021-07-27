package com.uwcs446.socialsports.services

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
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
        val request = FetchPlaceRequest.newInstance(placeId!!, placeFields)

        return placesClient.fetchPlace(request).await().place
    }
}
