package com.uwcs446.socialsports.services.user

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

    override suspend fun findById(id: String): User? {
        return usersCollection
            .document(id)
            .get()
            .await()
            .toUserEntity()
            ?.toDomain()
    }

    override suspend fun findByIds(ids: List<String>): List<User> {
        if (ids.isEmpty()) return emptyList()

        return usersCollection
            .whereIn(User::id.name, ids)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toUserEntity() }
            .toDomain()
    }

    override fun upsert(_user: User): User {
        val user = _user.toEntity()
        usersCollection.document(user.id).set(user)
            .addOnSuccessListener {
                Log.d(TAG, "Saved user ${user.id}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to save user ${user.id}")
            }
        return _user
    }
}

private fun DocumentSnapshot.toUserEntity() = this.toObject(UserEntity::class.java)
