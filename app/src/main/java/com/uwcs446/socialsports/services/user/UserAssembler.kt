package com.uwcs446.socialsports.services.user

import com.uwcs446.socialsports.domain.user.User

fun User.toEntity() =
    UserEntity(
        id = id,
        name = name
    )

fun UserEntity.toDomain() =
    User(
        id = id,
        name = name
    )

fun Collection<UserEntity>.toDomain() = this.map { it.toDomain() }
