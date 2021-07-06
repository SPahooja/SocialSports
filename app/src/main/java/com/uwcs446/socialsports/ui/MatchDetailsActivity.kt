package com.uwcs446.socialsports.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.uwcs446.socialsports.R
import java.util.ArrayList

class MatchDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_details)

        // Get match card data passed via recycler view
        val match: ArrayList<String>? = intent.getStringArrayListExtra("match")

        // Get TextViews from activity_match_details.xml
        val matchTitle: TextView = findViewById(R.id.match_title)
        val matchType: TextView = findViewById(R.id.match_type)
        val matchPlayerCount: TextView = findViewById(R.id.match_player_count)
        val matchDate: TextView = findViewById(R.id.match_date)
        val matchTime: TextView = findViewById(R.id.match_time)
        val matchLocationName: TextView = findViewById(R.id.match_location_name)
        val matchAddress: TextView = findViewById(R.id.match_address)

        // Update TextViews to show card data
        matchTitle.text = match?.get(0)
        matchType.text = match?.get(1)
        matchPlayerCount.text = match?.get(2)
        matchDate.text = match?.get(3)
        matchTime.text = match?.get(4)
        matchLocationName.text = match?.get(5)
        matchAddress.text = match?.get(6)
    }
}
