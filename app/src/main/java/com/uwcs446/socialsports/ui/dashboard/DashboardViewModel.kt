package com.uwcs446.socialsports.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
@Inject constructor(
    private val userRepository: CurrentAuthUserRepository,
) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value =
            "This is dashboard Fragment with user: [${userRepository.getUser()?.uid ?: "NO_USER"}]"
    }

    val text: LiveData<String> = _text
}
