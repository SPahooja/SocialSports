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
                .setLogo(R.drawable.ic_add_fab) // TODO add bg
                .setAvailableProviders(
                    arrayListOf(
                        AuthUI.IdpConfig.EmailBuilder()
                            .setRequireName(true)
                            .build(),
                        AuthUI.IdpConfig.PhoneBuilder() // TODO probably remove since no name set option
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
