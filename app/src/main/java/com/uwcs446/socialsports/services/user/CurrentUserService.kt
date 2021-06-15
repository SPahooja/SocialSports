package com.uwcs446.socialsports.services.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class CurrentUserService
@Inject constructor(
    private val firebaseAuth: FirebaseAuth
) :
    UserRepository {

    // Can also use auth state listeners
    // https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth.AuthStateListener


    private val _user = MutableLiveData<User>().apply {
        value = currentUser
    }

    override val user: LiveData<User> = _user

    private val currentFirebaseUser
        get() = firebaseAuth.currentUser

    private val currentUser
        get() = User.from(currentFirebaseUser)

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
