package com.uwcs446.socialsports.di.module

import android.content.Context
import com.uwcs446.socialsports.domain.location.LocationRepository
import com.uwcs446.socialsports.services.location.PlacesLocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class LocationModule {

    @Provides
    @ViewModelScoped
    fun locationRepository(
        @ApplicationContext activity: Context,
    ): PlacesLocationRepository = PlacesLocationRepository(activity)

    @Provides
    @ViewModelScoped
    fun provideLocationRepository(
        placesLocationRepository: PlacesLocationRepository
    ): LocationRepository = placesLocationRepository
}
