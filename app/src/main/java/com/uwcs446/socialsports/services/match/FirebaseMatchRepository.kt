package com.uwcs446.socialsports.services.match

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.MatchRepository
import javax.inject.Inject

class FirebaseMatchRepository
@Inject constructor(
    private val firestore: FirebaseFirestore
) : MatchRepository {

    private val TAG = this::class.simpleName

    override fun findAllByUser(userId: String): List<Match> {
        TODO("Not yet implemented")
    }

    override fun create(match: Match) {
        val matchCollection = firestore.collection("matches")
        val l = Tasks.await(Tasks.await(matchCollection.add(match.toEntity())).get())
        println(l.toObject(MatchEntity::class.java)?.toDomain())
    }

    override fun edit(match: Match) {
        TODO("Not yet implemented")
    }

    override fun delete(matchId: String) {
        TODO("Not yet implemented")
    }
}
