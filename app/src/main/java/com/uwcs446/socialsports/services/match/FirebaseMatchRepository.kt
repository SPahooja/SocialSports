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
    override suspend fun fetchExploreMatches(sport: Sport): List<Match> {
        val sportsToMatch = if (sport == Sport.ANY) Sport.values().toList() else listOf(sport)

        val matches = matchesCollection
            .whereIn(Match::sport.name, sportsToMatch)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }
            .toDomain()

        Log.d(TAG, "Found ${matches.size} matches")

        return matches
    }

    // TODO: Add filtering for future timestamp
    override suspend fun findAllByHost(hostId: String): List<Match> {
        val matches = matchesCollection
            .whereEqualTo(of(MatchEntity::hostId.name), hostId)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }
            .toDomain()

        Log.d(TAG, "Found ${matches.size} matches")

        return matches
    }

    // TODO: Add filtering for future timestamp
    override suspend fun findJoinedByUser(userId: String): List<Match> {
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

        val matches = (teamOneMatches + teamTwoMatches).toDomain()

        Log.d(TAG, "Found ${matches.size} matches")

        return matches
    }

    // TODO: Add filtering for past timestamp
    override suspend fun findPastWithUser(userId: String): List<Match> {
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

        val matches = (hostMatches + teamOneMatches + teamTwoMatches)
            .distinctBy { match -> match.id }
            .toDomain()

        Log.d(TAG, "Found ${matches.size} matches")

        return matches
    }

    override suspend fun fetchMatchById(matchId: String): Match? {
        return matchesCollection.document(matchId).get().await().toMatchEntity()?.toDomain()
    }

    override suspend fun joinMatch(matchId: String, userId: String, team: Int): Boolean {
        val match = fetchMatchById(matchId) ?: return false
        val teamSize = match.teamSize()

        // No-op if user already part of match
        if ((match.teamOne + match.teamTwo).contains(userId)) return false

        // Create new match with added user
        val newMatch = when (team) {
            1 -> match.copy(teamOne = match.teamOne.plus(userId))
            2 -> match.copy(teamTwo = match.teamTwo.plus(userId))
            else -> {
                Log.d(TAG, "Failed to add user $userId to team $team in match ${matchId}. Team must be either 1 or 2.")
                return false
            }
        }

        // Check if new match's team is still within size limits
        if (newMatch.teamOne.size > teamSize || newMatch.teamTwo.size > teamSize) {
            Log.d(TAG, "Failed to add user $userId to team $team in match ${matchId}. No space left")
            return false
        }

        // Write new match to db
        edit(newMatch)

        return true
    }

    override suspend fun leaveMatch(matchId: String, userId: String, team: Int): Boolean {
        val match = fetchMatchById(matchId) ?: return false

        // Create new match with removed user
        val newMatch = when (team) {
            1 -> match.copy(teamOne = match.teamOne.minus(userId))
            2 -> match.copy(teamTwo = match.teamTwo.minus(userId))
            else -> {
                Log.d(TAG, "Failed to remove user $userId from team $team in match ${matchId}. Team must be either 1 or 2.")
                return false
            }
        }

        // Write new match to db
        edit(newMatch)

        return true
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
