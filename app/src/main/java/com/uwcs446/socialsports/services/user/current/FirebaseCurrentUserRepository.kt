package com.uwcs446.socialsports.services.user.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.uwcs446.socialsports.domain.user.CurrentUserRepository
import com.uwcs446.socialsports.domain.user.User
import javax.inject.Inject

class FirebaseCurrentUserRepository
@Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : CurrentUserRepository {

    // Can also use auth state listeners
    // https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth.AuthStateListener

    private val _user = MutableLiveData<User>(currentUser)

    override val user: LiveData<User> = _user

    private val currentFirebaseUser
        get() = firebaseAuth.currentUser

    private val currentUser
        get() = currentFirebaseUser.toDomain()

    override fun getUser(): User? {
        return currentUser
    }

    override fun logout() {
        firebaseAuth
            .signOut()
            .also { refreshUser() }
    }

    override fun handleAuthChange() {
        // TODO handle user data changes, enforce signing in
        refreshUser()
    }

    private fun refreshUser() {
        _user.postValue(currentUser)
    }
}
