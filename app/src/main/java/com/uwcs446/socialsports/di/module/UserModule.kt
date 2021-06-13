package com.uwcs446.socialsports.di.module

import com.uwcs446.socialsports.services.user.CurrentUserService
import com.uwcs446.socialsports.services.user.UserRepository
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
    fun provideUserHelper(service: CurrentUserService): UserRepository = service
//
//    @Singleton
//    @Provides
//    fun provideContext(activity: Activity): Context = activity.baseContext
}
