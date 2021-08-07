package com.uwcs446.socialsports.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import com.uwcs446.socialsports.domain.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val currentUserRepository: CurrentAuthUserRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _rating = MutableLiveData<Float>()
    val rating: LiveData<Float> = _rating

    private val userObserver = Observer<FirebaseUser> {
        // TODO: fetch user's real name
        val authUser = currentUserRepository.getUser()
        _username.value = authUser?.displayName ?: "Logged out."
        viewModelScope.launch {
            _rating.value = userRepository.findById(authUser!!.uid)?.rating ?: 0F
        }
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
