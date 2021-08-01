package com.uwcs446.socialsports.domain.location

import com.google.android.libraries.places.api.model.Place

interface LocationRepository {
    suspend fun getPlace(placeId: String): Place
}
