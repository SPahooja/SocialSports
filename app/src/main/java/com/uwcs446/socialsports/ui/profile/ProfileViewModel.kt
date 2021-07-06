package com.uwcs446.socialsports.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.domain.user.CurrentUserRepository
import com.uwcs446.socialsports.domain.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val currentUserRepository: CurrentUserRepository,
    private val matchRepo: MatchRepository
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

    fun test() {
        matchRepo.create(
            Match(
                "id",
                Sport.BASKETBALL,
                "title",
                "description lalal allalal",
                LocalDate.now(),
                LocalTime.now(),
                Duration.ofMinutes(60),
                User("userID"),
                emptyList(),
                listOf(User("user1"), User("user2"))
            )
        )
    }

    private fun computeLabel() =
        """
            |This is profile Fragment with user: [${currentUserRepository.getUser()?.id ?: "NO_USER"}] 
        """.trimMargin()
}
