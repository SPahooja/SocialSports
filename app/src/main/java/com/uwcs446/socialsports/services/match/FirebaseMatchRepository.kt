package com.uwcs446.socialsports.services.match

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath.of
import com.google.firebase.firestore.FieldValue
import com.uwcs446.socialsports.di.module.MatchesCollection
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import com.uwcs446.socialsports.domain.match.Sport
import kotlinx.coroutines.tasks.await
import java.time.Duration
import java.time.Instant
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

    override suspend fun fetchExploreMatches(sport: Sport): List<Match> {
        val sportsToMatch = if (sport == Sport.ANY) Sport.values().toList() else listOf(sport)

        val matches = matchesCollection
            .whereIn(Match::sport.name, sportsToMatch)
            .whereGreaterThanOrEqualTo(MatchEntity::startTime.name, Instant.now().toEpochMilli())
            .orderBy(MatchEntity::startTime.name)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }
            .toDomain()

        Log.d(TAG, "Found ${matches.size} matches")

        return matches
    }

    override suspend fun fetchWithinDistance(curLatLng: LatLng?, distance: Int?): List<Match> {
        val start = Instant.now()
        val matches = matchesCollection
            .orderBy(MatchEntity::startTime.name)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }
            .toDomain()

//        return if (curLatLng != null) {
//            val curLocation = createLocation(curLatLng!!)
//            matches?.filter { m -> curLocation.distanceTo(createLocation(m.location.latLng)) < distance!!.times(1000) }
//        } else {
//            matches
//        }
        return if (curLatLng != null) {
            val curLocation = createGeoLocation(curLatLng)
            val distance = distance!!.times(1000)
            matches.filter { m ->
                GeoFireUtils.getDistanceBetween(
                    curLocation,
                    createGeoLocation(m.location.latLng)
                ) < distance
            }
        } else {
            matches
        }
            .also {
                println("Time ${Duration.between(start, Instant.now()).toMillis()} s")
            }
    }

//    suspend fun fetchWithinDistanceV2(curLatLng: LatLng, distanceInKM: Int): List<Match> {
//        val center = GeoLocation(curLatLng.latitude, curLatLng.longitude)
//        val radiusInM = (distanceInKM * 1000).toDouble()
//
//        val bounds = GeoFireUtils.getGeoHashQueryBounds(
//            center,
//            radiusInM
//        )
//
//        val queries = mutableListOf<Task<QuerySnapshot>>()
//        bounds.forEach {
//            queries.add(
//                matchesCollection
//                    .orderBy(of(MatchEntity::location.name, LocationEntity::geohash.name))
//                    .startAt(it.startHash)
//                    .endAt(it.endHash)
//                    .get()
//            )
//        }
//        val ans = mutableListOf<DocumentSnapshot>()
//        val l = Tasks.whenAllComplete(queries).await()
//            .forEach { t: Task<QuerySnapshot> ->
//                t.result
//                    .documents
// //                    .forEach { d ->
// //                        val lat = d.getDouble("lat") ?: Double.NaN
// //                        val lng = d.getDouble("lng") ?: Double.NaN
// //
// //                        // We have to filter out a few false positives due to GeoHash
// //                        // accuracy, but most will match
// //
// //                        // We have to filter out a few false positives due to GeoHash
// //                        // accuracy, but most will match
// //                        val docLocation = GeoLocation(lat, lng)
// //                        val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
// //                        if (distanceInM <= radiusInM) {
// //                            ans.add(d)
// //                        }
// //                    }
//            }

//        val matches = matchesCollection
//            .get()
//            .await()
//            .documents
//            .mapNotNull { document -> document.toMatchEntity() }
//            .toDomain()

//        return if (curLatLng != null) {
//            val curLocation = createLocation(curLatLng)
//            matches.filter { m ->
//                curLocation.distanceTo(createLocation(m.location.latLng)) < distanceInKM.times(1000)
//            }
//        } else {
//            matches
//        }
//    }

    override suspend fun fetchAfterDateTime(dateTime: Instant): List<Match> {
        val matches = matchesCollection
            .whereGreaterThanOrEqualTo(MatchEntity::startTime.name, dateTime.toEpochMilli())
            .orderBy(MatchEntity::startTime.name)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }
            .toDomain()

        Log.d(TAG, "Found ${matches.size} matches")

        return matches
    }

    override suspend fun findAllByHost(hostId: String): List<Match> {
        val matches = matchesCollection
            .whereEqualTo(MatchEntity::hostId.name, hostId)
            .whereGreaterThanOrEqualTo(MatchEntity::startTime.name, Instant.now().toEpochMilli())
            .orderBy(MatchEntity::startTime.name)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }
            .toDomain()

        Log.d(TAG, "Found ${matches.size} matches")

        return matches
    }

    override suspend fun findJoinedByUser(userId: String): List<Match> {
        val teamOneMatches = matchesCollection
            .whereArrayContains(MatchEntity::teamOne.name, userId)
            .whereGreaterThanOrEqualTo(MatchEntity::startTime.name, Instant.now().toEpochMilli())
            .orderBy(MatchEntity::startTime.name)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }
        val teamTwoMatches = matchesCollection
            .whereArrayContains(Match::teamTwo.name, userId)
            .whereGreaterThanOrEqualTo(MatchEntity::startTime.name, Instant.now().toEpochMilli())
            .orderBy(MatchEntity::startTime.name)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }

        val matches = (teamOneMatches + teamTwoMatches).toDomain()

        Log.d(TAG, "Found ${matches.size} matches")

        return matches
    }

    override suspend fun findPastWithUser(userId: String): List<Match> {
        val hostMatches = matchesCollection
            .whereEqualTo(of(MatchEntity::hostId.name), userId)
            .whereLessThan(MatchEntity::endTime.name, Instant.now().toEpochMilli())
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }
        val teamOneMatches = matchesCollection
            .whereArrayContains(MatchEntity::teamOne.name, userId)
            .whereLessThan(MatchEntity::endTime.name, Instant.now().toEpochMilli())
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }
        val teamTwoMatches = matchesCollection
            .whereArrayContains(Match::teamTwo.name, userId)
            .whereLessThan(MatchEntity::endTime.name, Instant.now().toEpochMilli())
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.toMatchEntity() }

        val matches = (hostMatches + teamOneMatches + teamTwoMatches)
            .distinctBy { matchEntity -> matchEntity.id }
            .sortedBy { matchEntity -> matchEntity.startTime }
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
        val teamName = when (team) {
            1 -> "teamOne"
            2 -> "teamTwo"
            else -> {
                Log.d(
                    TAG,
                    "Failed to add user $userId to team $team in match $matchId. Team must be either 1 or 2."
                )
                return false
            }
        }

        // No-op if user already part of match
        if ((match.teamOne + match.teamTwo).contains(userId)) return false

        // No-op if team is full
        val isFull = when (team) {
            1 -> match.teamOne.size >= teamSize
            2 -> match.teamOne.size >= teamSize
            else -> false
        }
        if (isFull) {
            Log.d(TAG, "Failed to add user $userId to team $team in match $matchId. No space left")
            return false
        }

        // Add user to team
        matchesCollection
            .document(match.id)
            .update(teamName, FieldValue.arrayUnion(userId))
            .await()

        return true
    }

    override suspend fun leaveMatch(matchId: String, userId: String, team: Int): Boolean {
        val match = fetchMatchById(matchId) ?: return false
        val teamName = when (team) {
            1 -> "teamOne"
            2 -> "teamTwo"
            else -> ""
        }

        // Remove user from team
        matchesCollection
            .document(match.id)
            .update(teamName, FieldValue.arrayRemove(userId))
            .await()

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

private fun createLocation(latLng: LatLng) =
    Location("")
        .apply {
            latitude = latLng.latitude
            longitude = latLng.longitude
        }

private fun createGeoLocation(latLng: LatLng) =
    GeoLocation(
        latLng.latitude,
        latLng.longitude
    )
// TODO add this logic to fetch users from match (in match details view)
// private fun allUsersFromMatches(matches: List<MatchEntity>): List<String> {
//    return matches.map { allUsersFromMatch(it) }.flatten().distinct()
// }
//
// private fun allUsersFromMatch(match: MatchEntity): List<String> {
//    return match.teamOne.plus(match.teamTwo)
// }
