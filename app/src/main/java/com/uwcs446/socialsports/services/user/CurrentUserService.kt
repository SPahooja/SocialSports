package com.uwcs446.socialsports.services.user

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class CurrentUserService @Inject constructor() : UserRepository {

    // Can also use auth state listeners https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth.AuthStateListener

    private val firebase = FirebaseAuth.getInstance()

    private val _user = MutableLiveData<User>().apply {
        value = currentUser
    }

    override val user: LiveData<User> = _user

    private val currentFirebaseUser
        get() = firebase.currentUser

    private val currentUser
        get() = User.from(currentFirebaseUser)

    override fun getUser(): User? {
        return currentUser
    }

    override fun logout() {
        firebase
            .signOut()
            .also { refreshUser() }
    }

    override fun login(activity: Activity) {
        if (currentFirebaseUser != null) return
        FirebaseUserLoginService
            .login(activity)
            .also { refreshUser() }
    }

    override fun handleAuthChange() {
        // TODO handle user data changes
        refreshUser()
    }

    private fun refreshUser() {
        _user.postValue(currentUser)
    }
}
