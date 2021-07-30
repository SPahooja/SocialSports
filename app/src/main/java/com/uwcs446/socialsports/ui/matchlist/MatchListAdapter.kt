package com.uwcs446.socialsports.ui.matchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.Place
import com.uwcs446.socialsports.MobileNavigationDirections
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.domain.match.Match
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MatchListAdapter() :
    RecyclerView.Adapter<MatchListAdapter.MatchViewHolder>() {

    private val matchList = mutableListOf<Pair<Match, Place>>()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.match_item_card, parent, false)
        return MatchViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        if (position < matchList.size) {
            val (match, place) = matchList[position]

            // TODO: update mapping based on the game item structure in gameList
            holder.matchTitle.text = match.title
            holder.matchType.text = match.sport.toString()
            holder.matchTypeIcon.setImageResource(match.sport.imageResource)
            holder.matchPlayerCount.text =
                "${match.currentPlayerCount()} / ${match.maxPlayerCount()}"
            holder.matchDate.text =
                match.startTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            holder.matchTime.text =
                match.startTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            holder.matchLocationName.text = place.name
            holder.matchAddress.text = place.address

            holder.itemView.setOnClickListener { view ->
                val action = MobileNavigationDirections.actionGlobalToMatchDetails(match.id)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    // Update match list
    fun updateMatchList(newMatchPlaces: List<Pair<Match, Place>>) {
        matchList.clear()
        matchList.addAll(newMatchPlaces)
        notifyDataSetChanged()
    }
}
