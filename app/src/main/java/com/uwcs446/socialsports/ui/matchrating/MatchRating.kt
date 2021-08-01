package com.uwcs446.socialsports.ui.matchrating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.ui.matchlist.MatchListUtils

class MatchRating : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_rating)

        val matchId = intent.getStringExtra("MatchId")
        // Placeholder find the match that matches the passed in ID
        val matches = MatchListUtils.genFakeMatchData(matchId!!.toInt())

        val match = matches.find { match -> match.id == matchId }

    }
}