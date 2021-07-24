package com.uwcs446.socialsports.services.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath.of
import com.uwcs446.socialsports.di.module.MatchesCollection
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.services.user.UserEntity
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMatchRepository
@Inject constructor(
    @MatchesCollection
    private val matchesCollection: CollectionReference
) : MatchRepository {

    private val TAG = this::class.simpleName

    private val _exploreMatches = MutableLiveData<List<Match>>()

    override val exploreMatches: LiveData<List<Match>> = _exploreMatches

    private val _matchesByHost = MutableLiveData<Pair<String, List<Match>>>()

    override val matchesByHost: LiveData<Pair<String, List<Match>>> = _matchesByHost

    // TODO Add filtering for future timestamp
    override suspend fun fetchExploreMatches(sport: Sport): List<Match>? {
        try {
            val matches = matchesCollection
                .whereIn(
                    Match::sport.name,
                    if (sport == Sport.ANY) Sport.values().toList() else listOf(sport)
                )
                .get()
                .await()
                .documents
                .mapNotNull { document -> document.toMatchEntity() }
            Log.d(TAG, "Found ${matches.size} matches")
            return matches.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "Something went wrong while fetching explore matches", e)
        }
        return null
    }

    // TODO: Add filtering for future timestamp
    override suspend fun findAllByHost(hostId: String): List<Match>? {
        try {
            val matches = matchesCollection
                .whereEqualTo(of(MatchEntity::hostId.name), hostId)
                .get()
                .await()
                .documents
                .mapNotNull { document -> document.toMatchEntity() }
            Log.d(TAG, "Found ${matches.size} matches")
            return matches.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "Something went wrong while fetching all matches by host $hostId", e)
        }
        return null
    }

    // TODO: Add filtering for future timestamp
    override suspend fun findJoinedByUser(userId: String): List<Match>? {
        try {
            val teamOneMatches = matchesCollection
                .whereArrayContains(MatchEntity::teamOne.name, userId)
                .get()
                .await()
                .documents
                .mapNotNull { document -> document.toMatchEntity() }
            val teamTwoMatches = matchesCollection
                .whereArrayContains(Match::teamTwo.name, userId)
                .get()
                .await()
                .documents
                .mapNotNull { document -> document.toMatchEntity() }
            val matches = (teamOneMatches + teamTwoMatches)
            Log.d(TAG, "Found ${matches.size} matches")
            return matches.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "Something went wrong while fetching matches for user $userId", e)
        }
        return null
    }

    // TODO: Add filtering for past timestamp
    override suspend fun findPastWithUser(userId: String): List<Match>? {
        try {
            val hostMatches = matchesCollection
                .whereEqualTo(of(MatchEntity::hostId.name), userId)
                .get()
                .await()
                .documents
                .mapNotNull { document -> document.toMatchEntity() }
            val teamOneMatches = matchesCollection
                .whereArrayContains(MatchEntity::teamOne.name, userId)
                .get()
                .await()
                .documents
                .mapNotNull { document -> document.toMatchEntity() }
            val teamTwoMatches = matchesCollection
                .whereArrayContains(Match::teamTwo.name, userId)
                .get()
                .await()
                .documents
                .mapNotNull { document -> document.toMatchEntity() }
            val matches =
                (hostMatches + teamOneMatches + teamTwoMatches).distinctBy { match -> match.id }
            Log.d(TAG, "Found ${matches.size} matches")
            return matches.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "Something went wrong while fetching matches for user $userId", e)
        }
        return null
    }

    override suspend fun fetchMatchWithUsers(userId: String): Match? {
        TODO("Not yet implemented")
    }

    override fun create(match: Match) = createOrSave(match.toEntity())

    override fun edit(match: Match) = createOrSave(match.toEntity())

    override fun delete(matchId: String) {
        matchesCollection.document(matchId).delete()
            .addOnSuccessListener {
                Log.d(TAG, "Deleted match $matchId")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to delete match $matchId")
            }
    }

    private fun createOrSave(match: MatchEntity) {
        matchesCollection.document(match.id).set(match)
            .addOnSuccessListener {
                Log.d(TAG, "Saved match ${match.id}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to save match ${match.id}")
            }
    }
}

private fun DocumentSnapshot.toMatchEntity() = this.toObject(MatchEntity::class.java)

// TODO add this logic to fetch users from match (in match details view)
// private fun allUsersFromMatches(matches: List<MatchEntity>): List<String> {
//    return matches.map { allUsersFromMatch(it) }.flatten().distinct()
// }
//
// private fun allUsersFromMatch(match: MatchEntity): List<String> {
//    return match.teamOne.plus(match.teamTwo)
// }
