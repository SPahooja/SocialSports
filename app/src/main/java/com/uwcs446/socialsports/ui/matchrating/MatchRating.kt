package com.uwcs446.socialsports.ui.matchrating

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.uwcs446.socialsports.R

class MatchRating : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_rating)
        val matchId = intent.getStringExtra("MatchId")
        if (matchId != null) {
            Log.d("match-rating-id", matchId)
        }
    }
}
