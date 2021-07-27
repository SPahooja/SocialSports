package com.uwcs446.socialsports.domain.user

import com.google.firebase.auth.FirebaseUser

interface CurrentAuthUserRepository {

    fun getUser(): FirebaseUser?

    fun logout()

    fun handleAuthChange()
}
