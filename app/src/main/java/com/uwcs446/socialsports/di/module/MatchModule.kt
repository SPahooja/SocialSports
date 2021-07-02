package com.uwcs446.socialsports.di.module

import com.google.firebase.firestore.FirebaseFirestore
import com.uwcs446.socialsports.services.match.MatchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MatchModule {

    @Provides
    @Singleton
    fun provideMatchRepository(firestore: FirebaseFirestore): MatchRepository =
        MatchRepository(firestore)
}
