package com.uwcs446.socialsports.services.user.current

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import com.uwcs446.socialsports.domain.user.User
import com.uwcs446.socialsports.domain.user.UserRepository
import javax.inject.Inject

class FirebaseCurrentUserRepository
@Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository
) : CurrentAuthUserRepository {

    private val TAG = this::class.simpleName

    private val _currentUser = MutableLiveData<FirebaseUser>(firebaseAuth.currentUser)
    override val user: LiveData<FirebaseUser> = _currentUser

    override fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
            .also { Log.d(TAG, "Fetched logged in user: ${it?.uid}") }
    }

    override fun logout() {
        firebaseAuth
            .signOut()
            .also { updateCurrentUser() }
            .also { Log.d(TAG, "User logged out") }
    }

    override suspend fun handleAuthChange() {
        updateCurrentUser()
        upsertUserCollection()
    }

    /**
     * Update the current logged-in user
     */
    private fun updateCurrentUser() {
        _currentUser.postValue(firebaseAuth.currentUser)
    }

    /**
     * Upsert this user into the users collection
     */
    private suspend fun upsertUserCollection() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null)
            userRepository.upsert(User(currentUser.uid, currentUser.displayName ?: "NO_NAME", 0F,0))
    }
}
