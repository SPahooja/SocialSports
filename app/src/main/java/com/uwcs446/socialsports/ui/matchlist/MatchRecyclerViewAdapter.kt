package com.uwcs446.socialsports.ui.matchlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.MobileNavigationDirections
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.domain.match.Match
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MatchRecyclerViewAdapter(private val matchList: List<Match>) :
    RecyclerView.Adapter<MatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.match_item_card, parent, false)
        return MatchViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        if (position < matchList.size) {
            val match = matchList[position]

            // TODO: update mapping based on the game item structure in gameList
            holder.matchTitle.text = match.title
            holder.matchType.text = match.sport.toString()
            holder.matchTypeIcon.setImageResource(match.sport.imageResource)
            holder.matchPlayerCount.text =
                "${match.currentPlayerCount()} / ${match.maxPlayerCount()}"
            holder.matchDate.text =
                match.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            holder.matchTime.text =
                match.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            holder.matchLocationName.text = "High Park" // TODO: add location name field
            holder.matchAddress.text =
                "1873 Bloor St W, Toronto, ON M6R 2Z" // TODO: add location address field

            holder.itemView.setOnClickListener { view ->
                val action = MobileNavigationDirections.actionGlobalToMatchDetails(match.id)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    fun notifyDataChanged(context: Context) {
        Toast.makeText(context, "data changed", Toast.LENGTH_SHORT).show()
    }
}
