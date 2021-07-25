package com.uwcs446.socialsports.ui.host.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.databinding.LocationItemBinding
import com.uwcs446.socialsports.domain.match.MatchLocation

class LocationListAdapter() :
    ListAdapter<MatchLocation, LocationListAdapter.LocationItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationItemViewHolder {
        val binding =
            LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class LocationItemViewHolder(
        private val binding: LocationItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { v: View ->
                val locationItem = getItem(adapterPosition)
                val action = HostLocationFragmentDirections.actionNavigationHostToHostEditDetails(
                    locationItem
                )
                Navigation.findNavController(v).navigate(action)
            }
        }

        fun bind(location: MatchLocation) {

            binding.apply {
                // TODO: fetch place name and address through placeId
                locationItemTitle.text = location.placeId
                locationItemAddress.text = location.latLng.toString()
//                locationItemDistance.text = location.distance
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MatchLocation>() {
        override fun areItemsTheSame(oldItem: MatchLocation, newItem: MatchLocation) =
            oldItem.placeId == newItem.placeId // TODO: maybe add a primary key id?

        override fun areContentsTheSame(oldItem: MatchLocation, newItem: MatchLocation) =
            oldItem == newItem
    }
}
