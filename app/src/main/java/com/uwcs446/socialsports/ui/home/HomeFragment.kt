package com.uwcs446.socialsports.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.databinding.FragmentHomeBinding
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.ui.matchlist.MatchRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val recyclerViewData = mutableListOf<Match>()
    private val recyclerViewAdapter = MatchRecyclerViewAdapter(recyclerViewData)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // set up recycler view for match list
        binding.layoutMatchList.recyclerviewMatch.setHasFixedSize(true)
        binding.layoutMatchList.recyclerviewMatch.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutMatchList.recyclerviewMatch.adapter = recyclerViewAdapter

        // Observer which updates the recyclerview when match data changes
        homeViewModel.userMatchList.observe(
            viewLifecycleOwner,
            {
                recyclerViewData.clear()
                recyclerViewData.addAll(it)
                recyclerViewAdapter.notifyDataSetChanged()
            }
        )

        // List of game filter toggle buttons
        val toggleButtons = listOf(
            binding.layoutHomeFilterToolbar.toggleButtonJoinedMatches,
            binding.layoutHomeFilterToolbar.toggleButtonHostingMatches,
            binding.layoutHomeFilterToolbar.toggleButtonPastMatches
        )

        // Set up toggle button listeners
        for (button in toggleButtons) {
            // Ensure that exactly one toggle button is active at a time
            button.setOnClickListener {
                for (toggleButton in toggleButtons) {
                    toggleButton.isChecked = false || toggleButton == button
                }
            }
        }

        // Filter matches based on toggle selection
        binding.layoutHomeFilterToolbar.toggleButtonJoinedMatches.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) homeViewModel.filterJoinedMatches()
        }
        binding.layoutHomeFilterToolbar.toggleButtonHostingMatches.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) homeViewModel.filterHostingMatches()
        }
        binding.layoutHomeFilterToolbar.toggleButtonPastMatches.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) homeViewModel.filterPastMatches()
        }

        // Initialize toggle buttons to view joined matches
        binding.layoutHomeFilterToolbar.toggleButtonJoinedMatches.isChecked = true

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
