package com.uwcs446.socialsports.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.services.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value =
            "This is dashboard Fragment with user: [${userRepository.getUser()?.id ?: "NO_USER"}]"
    }
    val text: LiveData<String> = _text
}
