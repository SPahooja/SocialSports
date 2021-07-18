package com.uwcs446.socialsports.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.databinding.FragmentHomeBinding
import com.uwcs446.socialsports.ui.matchlist.MatchRecyclerViewAdapter

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // set up recycler view for match list
        binding.layoutMatchList.recyclerviewMatch.setHasFixedSize(true)
        binding.layoutMatchList.recyclerviewMatch.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val adapter = homeViewModel.userMatchList.value?.let { MatchRecyclerViewAdapter(it) }
        binding.layoutMatchList.recyclerviewMatch.adapter = adapter

        // TODO: update observer which updates recyclerview when match data changes
        val gameListRecyclerView: RecyclerView = binding.layoutMatchList.recyclerviewMatch
        homeViewModel.userMatchList.observe(
            viewLifecycleOwner,
            {
                if (gameListRecyclerView.adapter == null) {
                    val recyclerViewAdapter = MatchRecyclerViewAdapter(it)
                    gameListRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                    gameListRecyclerView.adapter = recyclerViewAdapter
                } else {
                    gameListRecyclerView.adapter!!.notifyDataSetChanged()
                }
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

            // Filter matches based on toggle selection
            button.setOnCheckedChangeListener { _, _ ->
                // TODO: filter matches
            }
        }

        // Initialize toggle buttons to view joined matches
        binding.layoutHomeFilterToolbar.toggleButtonJoinedMatches.isChecked = true

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
