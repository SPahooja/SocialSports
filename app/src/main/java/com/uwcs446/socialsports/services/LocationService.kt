package com.uwcs446.socialsports.services

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LocationService @Inject constructor(
    @ApplicationContext private val activity: Context
) {
    suspend fun getPlace(placeId: String): Task<FetchPlaceResponse> {
        val placesClient = Places.createClient(activity)
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
        val request = FetchPlaceRequest.newInstance(placeId!!, placeFields)

        return placesClient.fetchPlace(request)
    }
}
