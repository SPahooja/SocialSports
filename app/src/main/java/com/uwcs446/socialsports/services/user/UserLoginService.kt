package com.uwcs446.socialsports.services.user

import android.app.Activity
import androidx.core.app.ActivityCompat
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.utils.RC_SIGN_IN

object UserLoginService {

    var isLoginActivityActive = false

    fun login(activity: Activity) {

        if (FirebaseAuth.getInstance().currentUser != null || isLoginActivityActive) return

        isLoginActivityActive = true

        ActivityCompat.startActivityForResult(
            activity,
            AuthUI
                .getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.sports_for_all_logo) // TODO add bg
                .setAvailableProviders(
                    arrayListOf(
                        AuthUI.IdpConfig.EmailBuilder()
                            .setRequireName(true)
                            .build(),
                        AuthUI.IdpConfig.GoogleBuilder()
                            .build(),
                    )
                )
                .build(),
            RC_SIGN_IN,
            null
        )
    }
}
