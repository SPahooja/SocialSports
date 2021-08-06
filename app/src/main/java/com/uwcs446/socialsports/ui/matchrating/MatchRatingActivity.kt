package com.uwcs446.socialsports.ui.matchrating

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.uwcs446.socialsports.R
import kotlinx.android.synthetic.main.activity_match_rating.*
import me.relex.circleindicator.CircleIndicator3

class MatchRatingActivity : AppCompatActivity() {

    private var userNameList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_rating)

        // Remove back button
        actionBar?.setDisplayHomeAsUpEnabled(false)

        val matchId = intent.getStringExtra("MatchId")
        if (matchId != null) {
            Log.d("match-rating-id", matchId)
        }

        postDummyToList()

        view_pager2.adapter = ViewPagerAdapter(userNameList)
        view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        val submitRatingButton: Button = findViewById(R.id.submit_rating_button)
        // Hook up indicator with the view pager
        indicator.setViewPager(view_pager2)

        // On Submit go to profile fragment
        submitRatingButton.setOnClickListener {
            finish()
        }
    }

    private fun addToList(userName: String) {
        userNameList.add(userName)
    }

    private fun postDummyToList() {
        for (i in 1..5) {
            addToList("Name $i")
        }
    }
}
