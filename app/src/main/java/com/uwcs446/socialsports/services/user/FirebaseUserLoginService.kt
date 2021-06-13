package com.uwcs446.socialsports.services.user

import android.app.Activity
import androidx.core.app.ActivityCompat
import com.firebase.ui.auth.AuthUI
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.RC_SIGN_IN

object FirebaseUserLoginService {

    fun login(activity: Activity) {

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
