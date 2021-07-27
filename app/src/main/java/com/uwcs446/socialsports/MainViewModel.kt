package com.uwcs446.socialsports

import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val userRepository: CurrentAuthUserRepository
) : ViewModel() {
    fun handleAuthChange() {
        userRepository.handleAuthChange()
    }
}
