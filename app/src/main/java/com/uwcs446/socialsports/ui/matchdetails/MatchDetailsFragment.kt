package com.uwcs446.socialsports.ui.matchdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.databinding.FragmentMatchDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class MatchDetailsFragment : Fragment() {

    private val args: MatchDetailsFragmentArgs by navArgs()
    private val matchDetailsViewModel: MatchDetailsViewModel by viewModels()
    private var _binding: FragmentMatchDetailsBinding? = null
    private val binding get() = _binding!!

    private val isHost: Boolean = true // TODO: set boolean

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMatchDetailsBinding.inflate(inflater, container, false)

        // Remove visibility of location in the summary
        binding.matchSummary.textMatchLocationName.visibility = GONE
        binding.matchSummary.textMatchAddress.visibility = GONE

        viewLifecycleOwner.lifecycleScope.launch {
            binding.matchProgressBar.visibility = VISIBLE
            binding.matchDetailsViews.visibility = INVISIBLE
            matchDetailsViewModel.fetchMatch(args.matchId!!)
        }

        val teamOneViewAdapter = TeamListAdapter(emptyList(), 0)
        binding.layoutTeamOne.recyclerviewTeam.setHasFixedSize(true)
        binding.layoutTeamOne.recyclerviewTeam.stopNestedScroll()
        binding.layoutTeamOne.recyclerviewTeam.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutTeamOne.recyclerviewTeam.adapter = teamOneViewAdapter

        val teamTwoViewAdapter = TeamListAdapter(emptyList(), 0)
        binding.layoutTeamTwo.recyclerviewTeam.setHasFixedSize(true)
        binding.layoutTeamTwo.recyclerviewTeam.stopNestedScroll()
        binding.layoutTeamTwo.recyclerviewTeam.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutTeamTwo.recyclerviewTeam.adapter = teamTwoViewAdapter

        matchDetailsViewModel.ready.observe(
            viewLifecycleOwner,
            {
                val match = matchDetailsViewModel.match.value
                val host = matchDetailsViewModel.host.value

                if (match != null && host != null) {
                    binding.matchSummary.textMatchTitle.setText(match.title)
                    binding.matchSummary.icMatchType.setImageResource(match.sport.imageResource)
                    binding.matchSummary.textMatchType.setText(match.sport.name)
                    binding.matchSummary.textMatchPlayerCount.setText("${match.currentPlayerCount()} / ${match.maxPlayerCount()}")
                    binding.matchSummary.textMatchDate.setText(match.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                    binding.matchSummary.textMatchTime.setText(match.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
                    binding.matchLocation.locationItemTitle.setText("High Park") // TODO: add location name field
                    binding.matchLocation.locationItemAddress.setText("1873 Bloor St W, Toronto, ON M6R 2Z") // TODO: add location address field
                    binding.matchLocation.locationItemDistance.setText("10km") // TODO: add distance
                    binding.matchDescription.setText(match.description)
                    binding.matchHostName.setText(host.name)

                    // Switch visibility of views
                    binding.matchProgressBar.visibility = GONE
                    binding.matchDetailsViews.visibility = VISIBLE

                    // TODO get the team from firestore
                    // set up recycler view for two teams
                    val teamSize = match.sport.teamSize
                    // TODO: replace mocks - get player name or some other identifier from match.teamOne and match.teamTwo
                    val teamOne = listOf("John Smith", "John Smith", "John Smith")
                    val teamTwo = listOf("John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith")

                    // display the edit button if current user is the match host
                    if (isHost) {
                        binding.editButtonMatchDetails.visibility = View.VISIBLE
                        binding.editButtonMatchDetails.setOnClickListener {
                            val action = MatchDetailsFragmentDirections.actionMatchDetailsToNavigationHost(match)
                            it.findNavController().navigate(action)
                        }
                    }
                }
            }
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
