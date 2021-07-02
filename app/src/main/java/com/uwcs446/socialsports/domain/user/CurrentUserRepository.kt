package com.uwcs446.socialsports.domain.user

import androidx.lifecycle.LiveData

interface CurrentUserRepository {

    val user: LiveData<User>

    fun getUser(): User?

    fun logout()

    fun handleAuthChange()
}
