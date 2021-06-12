package com.uwcs446.socialsports.services.user

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.legacy.coreutils.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.uwcs446.socialsports.RC_SIGN_IN

class CurrentUserService(activity: Activity) : Service() {
    private val parent = activity
    private val currentFirebaseUser: FirebaseUser? = fetchCurrentUser()

    private val currentUser: User? = null

    init {
        setupFirebase()
    }

    fun getUserOrThrow(): User {
        return User.from(currentFirebaseUser) ?: throw NoUserException()
    }

    private fun setupFirebase() {
        if (currentUser != null) {
            println("USER: $currentUser, ${currentUser.id}")
            return
        }
        startActivityForResult(
            parent,
            AuthUI
                .getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.notification_bg)
                .setAvailableProviders(
                    arrayListOf(
                        AuthUI.IdpConfig.EmailBuilder()
                            .setRequireName(true)
                            .build(),
                        AuthUI.IdpConfig.PhoneBuilder()
                            .build(),
                        AuthUI.IdpConfig.GoogleBuilder()
                            .build()
                    )
                )
                .build(),
            RC_SIGN_IN,
            null
        )
    }

    private fun fetchCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}
