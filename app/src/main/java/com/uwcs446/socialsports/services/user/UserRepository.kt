package com.uwcs446.socialsports.services.user

import android.app.Activity
import androidx.lifecycle.LiveData

interface UserRepository {

    val user: LiveData<User>

    fun getUser(): User?

    fun logout()

    fun login(activity: Activity)

    fun handleAuthChange()
}
