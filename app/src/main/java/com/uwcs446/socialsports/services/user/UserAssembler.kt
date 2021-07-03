package com.uwcs446.socialsports.services.user

import com.uwcs446.socialsports.domain.user.User

fun User.toEntity() =
    UserEntity(
        id = id
    )

fun UserEntity.toDomain() =
    User(
        id = id
    )