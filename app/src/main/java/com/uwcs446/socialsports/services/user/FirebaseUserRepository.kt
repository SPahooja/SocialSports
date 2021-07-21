package com.uwcs446.socialsports.services.user

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.uwcs446.socialsports.di.module.UsersCollection
import com.uwcs446.socialsports.domain.user.User
import com.uwcs446.socialsports.domain.user.UserRepository
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository(
    @UsersCollection
    private val usersCollection: CollectionReference
) : UserRepository {

    private val TAG = this::class.simpleName

    override suspend fun findById(ids: List<String>): List<User> {
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
}

private fun DocumentSnapshot.toUser(): User? = this.toUserEntity()?.toDomain()

private fun DocumentSnapshot.toUserEntity() = this.toObject(UserEntity::class.java)
