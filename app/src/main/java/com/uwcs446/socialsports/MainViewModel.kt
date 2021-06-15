package com.uwcs446.socialsports

import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.services.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun handleAuthChange() {
        userRepository.handleAuthChange()
    }

}
