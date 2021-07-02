package com.uwcs446.socialsports.domain.user

interface UserRepository {
    fun findAll(): List<User>
}
