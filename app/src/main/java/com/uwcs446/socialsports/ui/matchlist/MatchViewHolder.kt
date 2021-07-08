package com.uwcs446.socialsports.ui.matchlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.R

class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var matchTitle: TextView = itemView.findViewById(R.id.text_match_title)
    var matchType: TextView = itemView.findViewById(R.id.text_match_type)
    var matchTypeIcon: ImageView = itemView.findViewById(R.id.ic_match_type)
    var matchPlayerCount: TextView = itemView.findViewById(R.id.text_match_player_count)
    var matchDate: TextView = itemView.findViewById(R.id.text_match_date)
    var matchTime: TextView = itemView.findViewById(R.id.text_match_time)
    var matchLocationName: TextView = itemView.findViewById(R.id.text_match_location_name)
    var matchAddress: TextView = itemView.findViewById(R.id.text_match_address)
}
