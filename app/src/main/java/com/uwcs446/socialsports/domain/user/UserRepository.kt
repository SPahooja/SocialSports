package com.uwcs446.socialsports.domain.user

interface UserRepository {

    /**
     * Given a user ID, return the corresponding User object if it exists.
     */
    suspend fun findById(id: String): User?

    /**
     * Given a list of user IDs, return a list of corresponding User objects.
     *
     * Note: the length of the returned list may not be the same size as the
     *   input array if users with a given ID do not exist.
     */
    suspend fun findByIds(ids: List<String>): List<User>

    /**
     * Upsert (update or insert) a user
     */
    fun upsert(user: User): User
}
