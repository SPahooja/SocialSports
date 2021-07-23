package com.uwcs446.socialsports.services.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.uwcs446.socialsports.di.module.UsersCollection
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.user.CurrentAuthUserRepository
import com.uwcs446.socialsports.domain.user.User
import com.uwcs446.socialsports.domain.user.UserMatchDetails
import com.uwcs446.socialsports.domain.user.UserRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository(
    @UsersCollection
    private val usersCollection: CollectionReference,
    private val currentUserRepository: CurrentAuthUserRepository,
    private val matchesRepository: MatchRepository
) : UserRepository {

    private val TAG = this::class.simpleName

    private val _user = MutableLiveData<User>()

    override val user: LiveData<User> = _user

    private val _userMatches = MutableLiveData<UserMatchDetails>()

    private val userId
        get() = currentUserRepository.getUser()?.uid

    init {
        refreshUserDetails()
        setupUserSnapshotListener()
    }

    override suspend fun findByIds(ids: List<String>): List<User> {
        return try {
            usersCollection
                .whereIn(User::id.name, ids)
                .get()
                .await()
                .documents
                .mapNotNull { document -> document.toUser() }
        } catch (e: Exception) {
            Log.e(TAG, "Something went wrong while fetching users $ids", e)
            emptyList()
        }
    }

    private fun setupUserSnapshotListener() {
        userId?.let {
            usersCollection
                .document(it)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        refreshUserDetails()
                    } else {
                        Log.e(TAG, "Current User: null")
                    }
                }
        }
    }

    private fun refreshUserDetails() {
        refreshUser()
        refreshUserMatches()
    }

    private fun refreshUserMatches() {
        val userMatches = user.value?.matches ?: return
        val matchIds = userMatches.host.plus(userMatches.player).distinct()
        runBlocking {
            val matches = matchesRepository.findByIds(matchIds)
            val matchDetails = UserMatchDetails(
                player = matches.filterNotNull().filter { userMatches.player.contains(it.id) },
                host = matches.filterNotNull().filter { userMatches.host.contains(it.id) }
            )
            _userMatches.postValue(matchDetails)
        }
    }

    private fun refreshUser() {
        userId?.let {
            usersCollection
                .document(it)
                .get()
                .addOnSuccessListener { document ->
                    _user.postValue(document.toUser())
                }

        }

    }

}

private fun DocumentSnapshot.toUser(): User? = this.toUserEntity()?.toDomain()

private fun DocumentSnapshot.toUserEntity() = this.toObject(UserEntity::class.java)
