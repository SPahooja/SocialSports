package com.uwcs446.socialsports.domain.user

interface UserRepository {

    suspend fun findById(ids: List<String>): List<User>

}
