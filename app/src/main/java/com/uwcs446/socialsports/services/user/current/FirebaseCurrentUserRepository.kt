package com.uwcs446.socialsports.services.user.current

import android.util.Log
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

    private val TAG = this::class.simpleName

    private val _user = MutableLiveData<User>(currentUser)

    override val user: LiveData<User> = _user

    private val currentFirebaseUser
        get() = firebaseAuth.currentUser

    private val currentUser
        get() = currentFirebaseUser.toDomain()
            .also { Log.d(TAG, "Current User: $") }

    override fun getUser(): User? {
        Log.d(TAG, "Fetch logged in user: ${currentUser?.id}")
        return currentUser
    }

    override fun logout() {
        firebaseAuth
            .signOut()
            .also { refreshUser() }
            .also { Log.d(TAG, "User logged out") }
    }

    override fun handleAuthChange() {
        // TODO handle user data changes, enforce signing in
        refreshUser()
    }

    private fun refreshUser() {
        _user.postValue(currentUser)
    }
}
