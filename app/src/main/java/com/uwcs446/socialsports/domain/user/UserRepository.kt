package com.uwcs446.socialsports.domain.user

interface UserRepository {

    suspend fun findById(id: String): User?

    suspend fun findByIds(ids: List<String>): List<User>
}
