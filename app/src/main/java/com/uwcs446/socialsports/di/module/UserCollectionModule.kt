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
class UserCollectionModule {

    @Provides
    @Singleton
    @UsersCollection
    fun userCollection(firestore: FirebaseFirestore) = firestore.collection("users")
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersCollection
