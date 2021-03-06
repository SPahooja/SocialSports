package com.uwcs446.socialsports.ui.matchdetails

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.TeamPositionRowBinding
import java.lang.Integer.min

class TeamListAdapter() :
    RecyclerView.Adapter<TeamListAdapter.TeamPositionViewHolder>() {

    private val joinedUsers = mutableListOf<String>()
    private var teamSize = 0

    // View Holder class
    class TeamPositionViewHolder(private val binding: TeamPositionRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(identifier: String) {
            with(binding) {
                textPlayerIdentifier.text = identifier
                icTeamPlayer.setImageResource(R.drawable.ic_baseline_account_circle_50)
            }
        }
    }

    // Create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamPositionViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = TeamPositionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TeamPositionViewHolder(binding)
    }

    // Replace the contents of a view
    override fun onBindViewHolder(holder: TeamPositionViewHolder, position: Int) {
        if (position < joinedUsers.size) {
            holder.bind(joinedUsers[position])

            // TODO: on clicking a team member
            holder.itemView.setOnClickListener {
                Log.d("join", "Existing user!")
            }
        }
    }

    // Return the size of your dataset
    override fun getItemCount() = min(teamSize, joinedUsers.size)

    // Update joinedUsers
    fun updateTeamMembers(users: List<String>) {
        joinedUsers.clear()
        joinedUsers.addAll(users)
        notifyDataSetChanged()
    }

    // Update team size
    fun updateTeamSize(size: Int) {
        teamSize = size
        notifyDataSetChanged()
    }
}
