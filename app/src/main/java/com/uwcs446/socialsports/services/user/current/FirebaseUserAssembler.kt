package com.uwcs446.socialsports.services.user.current

import com.google.firebase.auth.FirebaseUser
import com.uwcs446.socialsports.domain.user.User

fun FirebaseUser?.toDomain(): User? {
    this ?: return null
    return User(
        id = this.uid,
        name = "John Smith" // Get this from firestore
    )
}
