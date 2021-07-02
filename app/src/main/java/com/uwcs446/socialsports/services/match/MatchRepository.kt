package com.uwcs446.socialsports.services.match

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class MatchRepository
@Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val TAG = this::class.simpleName
}
