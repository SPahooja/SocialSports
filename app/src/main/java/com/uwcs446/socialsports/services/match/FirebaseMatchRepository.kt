package com.uwcs446.socialsports.services.match

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

    override fun create(match: Match): Match {
        TODO("Not yet implemented")
    }

    override fun edit(match: Match): Match {
        TODO("Not yet implemented")
    }

    override fun delete(matchId: String) {
        TODO("Not yet implemented")
    }
}
