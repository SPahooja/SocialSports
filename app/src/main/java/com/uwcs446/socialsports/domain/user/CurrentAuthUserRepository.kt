package com.uwcs446.socialsports.domain.user

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser

interface CurrentAuthUserRepository {

    val user: LiveData<FirebaseUser>

    /**
     * Returns current logged-in user.
     *
     * Note: A user's real name cannot be determined from FirebaseUser. Query the
     *   users collection with this user's UID to find their real name.
     */
    fun getUser(): FirebaseUser?

    /**
     * Logs out the current user.
     */
    fun logout()

    fun handleAuthChange()
}
