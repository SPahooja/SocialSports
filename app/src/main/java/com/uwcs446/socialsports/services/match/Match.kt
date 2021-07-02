package com.uwcs446.socialsports.services.match

import com.uwcs446.socialsports.services.user.User

data class Match(
    val id: String,
    val type: MatchType,
    val host: User,
    val participants: Array<User>
) {

    // TODO (simon): connect to firestore

    /**
     * Autogenerated equals() method (recommended by IDE since .participants is an array)
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Match

        if (id != other.id) return false
        if (type != other.type) return false
        if (host != other.host) return false
        if (!participants.contentEquals(other.participants)) return false

        return true
    }

    /**
     * Autogenerated hasCode() method (recommended by IDE since .participants is an array)
     */
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + host.hashCode()
        result = 31 * result + participants.contentHashCode()
        return result
    }
}
