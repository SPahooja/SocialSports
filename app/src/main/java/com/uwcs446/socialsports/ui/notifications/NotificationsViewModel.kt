package com.uwcs446.socialsports.ui.notifications

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.services.user.User
import com.uwcs446.socialsports.services.user.UserRepository
import com.uwcs446.socialsports.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel
@Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val userObserver = Observer<User> {
        _text.setValue(computeLabel())
    }

    private val _text = MutableLiveData<String>().apply {
        value = computeLabel()
    }
    val text: LiveData<String> = _text

    init {
        userRepository.user.observeForever(userObserver)
    }

    fun handleLogout() {
        userRepository.logout()
    }

    fun handleLogin(activity: Activity) {
        userRepository.login(activity)
    }

    fun testResource(): Resource<String> {
        return Resource.Loading("llll")
    }

    override fun onCleared() {
        userRepository.user.removeObserver(userObserver)
        super.onCleared()
    }

    private fun computeLabel() =
        "This is notifications Fragment with user: [${userRepository.getUser()?.id ?: "NO_USER"}]"
}
