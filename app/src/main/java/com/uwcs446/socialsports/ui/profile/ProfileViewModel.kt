package com.uwcs446.socialsports.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.user.CurrentUserRepository
import com.uwcs446.socialsports.domain.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val currentUserRepository: CurrentUserRepository,
) : ViewModel() {

    private val userObserver = Observer<User> {
        _text.setValue(computeLabel())
    }

    private val _text = MutableLiveData(computeLabel())
    val text: LiveData<String> = _text

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

    private fun computeLabel() =
        """
            |This is profile Fragment with user: [${currentUserRepository.getUser()?.id ?: "NO_USER"}] 
        """.trimMargin()
}