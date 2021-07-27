package com.uwcs446.socialsports.services.user.current

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import javax.inject.Inject

class FirebaseCurrentUserRepository
@Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : CurrentAuthUserRepository {

    private val TAG = this::class.simpleName

    override fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
            .also {
                Log.d(TAG, "Fetch logged in user: ${it?.uid}")
            }
    }

    override fun logout() {
        firebaseAuth
            .signOut()
            .also { Log.d(TAG, "User logged out") }
    }

    override fun handleAuthChange() {
        // TODO handle user data changes, enforce signing in
//        refreshUser()
    }
}
