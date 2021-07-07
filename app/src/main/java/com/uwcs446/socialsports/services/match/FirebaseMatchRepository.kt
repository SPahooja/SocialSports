package com.uwcs446.socialsports.services.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.uwcs446.socialsports.di.module.MatchesCollection
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.services.user.UserEntity
import javax.inject.Inject

class FirebaseMatchRepository
@Inject constructor(
    @MatchesCollection
    private val collection: CollectionReference,
) : MatchRepository {

    private val TAG = this::class.simpleName

    private val _exploreMatches = MutableLiveData<List<Match>>()

    override val exploreMatches: LiveData<List<Match>> = _exploreMatches

    private val _matchesByHost = MutableLiveData<Pair<String, List<Match>>>()

    override val matchesByHost: LiveData<Pair<String, List<Match>>> = _matchesByHost

    override fun fetchExploreMatches() {
        collection
            .get()
            .addOnSuccessListener { result ->
                val matches = result
                    .documents
                    .mapNotNull {
                        it.toMatch()
                    }
                _exploreMatches.postValue(matches)
                    .also { Log.d(TAG, "Retrieved ${matches.size} explore matches") }
            }
            .addOnFailureListener {
                Log.d(TAG, "Something went wrong fetching explore matches")
            }
    }

    override fun findAllByHost(hostId: String) {
        collection
            .whereEqualTo(
                FieldPath.of(
                    MatchEntity::host.name,
                    UserEntity::id.name
                ),
                hostId
            ).get()
            .addOnSuccessListener { result ->
                val matchesByHost = result
                    .documents
                    .mapNotNull {
                        it.toMatch()
                    }
                _matchesByHost.postValue(Pair(hostId, matchesByHost))
                    .also { Log.d(TAG, "Retrieved ${matchesByHost.size} for host user: $hostId") }
            }
            .addOnFailureListener {
                Log.d(TAG, "Something went wrong")
            }
    }

    override fun create(match: Match) {
        collection.document(match.id).set(match.toEntity())
            .addOnSuccessListener {
                Log.d(TAG, "Created match ${match.id}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create match ${match.id}")
            }
    }

    override fun edit(match: Match) {
        TODO("Not yet implemented")
    }

    override fun delete(matchId: String) {
        TODO("Not yet implemented")
    }
}

private fun DocumentSnapshot.toMatch() = this.toObject(MatchEntity::class.java)?.toDomain()
