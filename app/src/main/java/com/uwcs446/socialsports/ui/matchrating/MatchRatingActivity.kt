package com.uwcs446.socialsports.ui.matchrating

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.uwcs446.socialsports.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_match_rating.*
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3

@AndroidEntryPoint
class MatchRatingActivity : AppCompatActivity() {

    private val matchRatingViewModel: MatchRatingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_rating)

        // Remove back button
        actionBar?.setDisplayHomeAsUpEnabled(false)

        val matchId = intent.getStringExtra("MatchId")

        lifecycleScope.launch {
            matchRatingViewModel.fetchMatchPlayers(matchId!!)
        }

        matchRatingViewModel.ready.observe(
            this,
            { ready ->
                if (ready) {

                    val players = matchRatingViewModel.players.value

                    val matchRatingAdapter = players?.let { ViewPagerAdapter(it) }
                    view_pager2.adapter = matchRatingAdapter
                    view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                    val indicator = findViewById<CircleIndicator3>(R.id.indicator)
                    val submitRatingButton: Button = findViewById(R.id.submit_rating_button)
                    // Hook up indicator with the view pager
                    indicator.setViewPager(view_pager2)

                    // OnSubmit go home fragment
                    submitRatingButton.setOnClickListener {
                        if (matchRatingAdapter != null) {
                            matchRatingViewModel.ratePlayers(matchRatingAdapter.getPlayerRatingList())
                        }
                        finish()
                    }
                }
            }
        )
    }
}
