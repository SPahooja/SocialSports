package com.uwcs446.socialsports.ui.gamelist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.R

class MatchRecyclerViewAdapter(private val matchList: List<List<String>>): RecyclerView.Adapter<MatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.match_item_card, parent, false)
        return MatchViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        if (position < matchList.size) {
            val match = matchList[position]

            //TODO: update mapping based on the game item structure in gameList
            holder.matchTitle.text = match[0]
            holder.matchType.text= match[1]
            holder.matchTypeIcon.setImageResource(R.drawable.ic_sports_soccer)
            holder.matchPlayerCount.text = match[2]
            holder.matchDate.text = match[3]
            holder.matchTime.text = match[4]
            holder.matchLocationName.text = match[5]
            holder.matchAddress.text = match[6]
        }
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    fun notifyDataChanged(context: Context) {
        Toast.makeText(context, "data changed", Toast.LENGTH_SHORT).show()
    }

}