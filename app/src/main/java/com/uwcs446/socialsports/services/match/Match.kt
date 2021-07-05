package com.uwcs446.socialsports.services.match

import com.uwcs446.socialsports.domain.user.User
import java.time.LocalDateTime

data class Match(
    val id: String,
    val type: MatchType,
    val title: String,
    val description: String,
    val time: LocalDateTime,
    val capacity: Int,
    val host: User,
    val participants: Set<User>,
) {
    // TODO (simon): connect to firestore
}
