package com.uwcs446.socialsports.services.user

import com.uwcs446.socialsports.domain.user.User
import com.uwcs446.socialsports.domain.user.UserRepository

class FirebaseUserRepository : UserRepository {
    override fun findAll(): List<User> {
        return listOf(
            User(
                id = "id1"
            ),
            User(
                id = "id2"
            )
        )
    }
}