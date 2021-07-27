package com.uwcs446.socialsports.ui.matchdetails

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.TeamPositionRowBinding

class TeamListAdapter(private val joinedUsers: List<String>, private val teamSize: Int) :
    RecyclerView.Adapter<TeamListAdapter.TeamPositionViewHolder>() {

    // View Holder class
    class TeamPositionViewHolder(val binding: TeamPositionRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(identifier: String) {
            with(binding) {
                if (identifier.isEmpty()) {
                    textPlayerIdentifier.text = "Join"
                    icTeamPlayer.setImageResource(R.drawable.ic_baseline_person_add_24px)
                } else {
                    textPlayerIdentifier.text = identifier
                    icTeamPlayer.setImageResource(R.drawable.ic_baseline_account_circle_50)
                }
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
        } else if (position < teamSize) {
            holder.bind("")

            // TODO: onClick for joining
            holder.itemView.setOnClickListener { view ->
                Log.d("join", "Joined!")
            }
        }
    }

    // Return the size of your dataset
    override fun getItemCount() = teamSize
}
