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

    override suspend fun findById(id: String): User? {
        return try {
            usersCollection
                .document(id)
                .get()
                .await()
                .toUserEntity()
                ?.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "Something went wrong while fetching user $id", e)
            null
        }
    }

    override suspend fun findByIds(ids: List<String>): List<User> {
        if (ids.isEmpty()) {
            return emptyList()
        }
        return try {
            val users = usersCollection
                .whereIn(User::id.name, ids)
                .get()
                .await()
                .documents
                .mapNotNull { document -> document.toUserEntity() }
            return users.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "Something went wrong while fetching users $ids", e)
            emptyList()
        }
    }
}

private fun DocumentSnapshot.toUserEntity() = this.toObject(UserEntity::class.java)
