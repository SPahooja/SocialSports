package com.uwcs446.socialsports.services.user

import java.util.UUID

data class UserEntity(
    val id: String = UUID.randomUUID().toString(),
    val matches: UserMatchesEntity = UserMatchesEntity()
)

data class UserMatchesEntity(
    val player: List<String> = emptyList(),
    val host: List<String> = emptyList()
)
