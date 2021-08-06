package com.uwcs446.socialsports.ui.matchrating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.R

class ViewPagerAdapter(private var userNames: List<String>) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    private var userRatingList = MutableList(userNames.size) { 0F }

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userNameText: TextView = itemView.findViewById(R.id.user_name)
        private val userRatingBar: RatingBar = itemView.findViewById(R.id.user_rating)

        init {
            userRatingBar.setOnRatingBarChangeListener { ratingBar: RatingBar, fl: Float, b: Boolean ->
                val position = adapterPosition
                userRatingList[position] = fl
                Toast.makeText(itemView.context, "you clicked on item #${position + 1}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        holder.userNameText.text = userNames[position]
    }

    override fun getItemCount(): Int {
        return userNames.size
    }
}
