package com.uwcs446.socialsports.domain.user

import androidx.lifecycle.LiveData

interface UserRepository {

    val user: LiveData<User>

    suspend fun findByIds(ids: List<String>): List<User>
}
