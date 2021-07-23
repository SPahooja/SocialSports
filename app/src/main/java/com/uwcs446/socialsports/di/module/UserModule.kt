package com.uwcs446.socialsports.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import com.uwcs446.socialsports.domain.user.UserRepository
import com.uwcs446.socialsports.services.user.FirebaseUserRepository
import com.uwcs446.socialsports.services.user.current.FirebaseCurrentUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    @Provides
    @Singleton
    fun currentUserRepository(firebaseAuth: FirebaseAuth): FirebaseCurrentUserRepository =
        FirebaseCurrentUserRepository(firebaseAuth)

    @Provides
    @Singleton
    fun provideCurrentUserRepository(firebaseRepository: FirebaseCurrentUserRepository): CurrentAuthUserRepository =
        firebaseRepository

    @Provides
    @Singleton
    fun userRepository(
        @UsersCollection users: CollectionReference,
        currentUserRepository: CurrentAuthUserRepository,
        matchRepository: MatchRepository
    ): FirebaseUserRepository =
        FirebaseUserRepository(users, currentUserRepository, matchRepository)

    @Provides
    @Singleton
    fun provideUserRepository(firebaseRepository: FirebaseUserRepository): UserRepository =
        firebaseRepository
}
