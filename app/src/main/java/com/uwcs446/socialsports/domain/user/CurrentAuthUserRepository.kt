package com.uwcs446.socialsports.domain.user

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser

interface CurrentAuthUserRepository {

    val user: LiveData<FirebaseUser>

    /**
     * Returns current logged-in user.
     */
    fun getUser(): FirebaseUser?

    /**
     * Logs out the current user.
     */
    fun logout(context: Context)

    suspend fun handleAuthChange()
}
