package com.uwcs446.socialsports.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val currentUserRepository: CurrentAuthUserRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _rating = MutableLiveData<Float>()
    val rating: LiveData<Float> = _rating

    private val userObserver = Observer<FirebaseUser> {
        // TODO: fetch user's real name
        _username.value = currentUserRepository.getUser()?.displayName ?: "Logged out."
        // TODO: fetch user's rating
        _rating.value = 3.5F
    }

    init {
        currentUserRepository.user.observeForever(userObserver)
    }

    fun handleLogout() {
        currentUserRepository.logout()
    }

    override fun onCleared() {
        currentUserRepository.user.removeObserver(userObserver)
        super.onCleared()
    }
}
