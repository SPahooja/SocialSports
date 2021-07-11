package com.uwcs446.socialsports.di.module

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MatchCollectionModule {

    @Provides
    @Singleton
    @MatchesCollection
    fun matchCollection(firestore: FirebaseFirestore) = firestore.collection("matches")
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MatchesCollection
