package com.uwcs446.socialsports.services.match

import com.google.type.DateTime
import com.uwcs446.socialsports.services.user.User

data class Match(
    val id: String,
    val type: MatchType,
    val title: String,
    val description: String,
    val time: DateTime,
    val host: User,
    val participants: Set<User>,
) {
    // TODO (simon): connect to firestore
}
