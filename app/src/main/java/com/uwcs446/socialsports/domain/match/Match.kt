package com.uwcs446.socialsports.domain.match

import com.google.type.DateTime
import com.uwcs446.socialsports.domain.user.User
import com.uwcs446.socialsports.services.match.MatchType
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
}
