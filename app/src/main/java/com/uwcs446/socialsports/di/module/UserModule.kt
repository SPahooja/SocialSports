package com.uwcs446.socialsports.di.module

import com.google.firebase.auth.FirebaseAuth
import com.uwcs446.socialsports.domain.user.CurrentUserRepository
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
    fun provideCurrentUserRepository(firebaseRepository: FirebaseCurrentUserRepository): CurrentUserRepository =
        firebaseRepository

    @Provides
    @Singleton
    fun userRepository(): FirebaseUserRepository =
        FirebaseUserRepository()

    @Provides
    @Singleton
    fun provideUserRepository(firebaseRepository: FirebaseUserRepository): UserRepository =
        firebaseRepository
}
