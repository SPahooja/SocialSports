package com.uwcs446.socialsports.services.match

import com.uwcs446.socialsports.services.user.User

data class Match(
    val id: String,
    val type: MatchType,
    val host: User,
    val participants: Set<User>
) {
    // TODO (simon): connect to firestore
}
