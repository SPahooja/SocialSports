package com.uwcs446.socialsports.di.module

import com.google.firebase.firestore.FirebaseFirestore
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.services.match.FirebaseMatchRepository
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
    fun matchRepository(firestore: FirebaseFirestore): FirebaseMatchRepository =
        FirebaseMatchRepository(firestore)

    @Provides
    @Singleton
    fun provideMatchRepository(firebaseMatchRepository: FirebaseMatchRepository): MatchRepository =
        firebaseMatchRepository
}
