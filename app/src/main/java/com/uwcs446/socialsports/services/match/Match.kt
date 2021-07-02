package com.uwcs446.socialsports.services.match

import com.google.type.DateTime
import com.uwcs446.socialsports.services.user.UserEntity

data class Match(
    val id: String,
    val type: MatchType,
    val title: String,
    val description: String,
    val time: DateTime,
    val host: UserEntity,
    val participants: Set<UserEntity>,
) {
    // TODO (simon): connect to firestore
}
