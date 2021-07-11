package com.uwcs446.socialsports.services.user

import java.util.UUID

data class UserEntity(
    val id: String = UUID.randomUUID().toString(),
)
