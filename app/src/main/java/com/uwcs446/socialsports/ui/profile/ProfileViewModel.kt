package com.uwcs446.socialsports.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import com.uwcs446.socialsports.domain.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val currentUserRepository: CurrentAuthUserRepository,
    private val matchRepo: MatchRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _rating = MutableLiveData<Float>()
    val rating: LiveData<Float> = _rating

    private val userObserver = Observer<User> {
        _username.setValue(
            currentUserRepository.getUser()?.uid ?: "NO_USER"
        ) // TODO: fetch username
        _rating.setValue(3.5F) // TODO: fetch user rating
    }

    init {
//        currentUserRepository.user.observeForever(userObserver)
    }

    fun handleLogout() {
        currentUserRepository.logout()
    }

    override fun onCleared() {
//        currentUserRepository.user.removeObserver(userObserver)
//        super.onCleared()
    }
}
