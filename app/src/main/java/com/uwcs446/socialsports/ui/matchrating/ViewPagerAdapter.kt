package com.uwcs446.socialsports.ui.matchrating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.domain.user.User

class ViewPagerAdapter(private var userNames: List<User>) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    private var userRatingList = MutableList(userNames.size) { 0F }

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userNameText: TextView = itemView.findViewById(R.id.user_name)
        private val userRatingBar: RatingBar = itemView.findViewById(R.id.user_rating)

        init {
            userRatingBar.setOnRatingBarChangeListener { ratingBar: RatingBar, fl: Float, b: Boolean ->
                val position = adapterPosition
                userRatingList[position] = fl
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
        holder.userNameText.text = userNames[position].name
    }

    override fun getItemCount(): Int {
        return userNames.size
    }

    fun getPlayerRatingList(): MutableList<Float> {
        return userRatingList
    }
}
