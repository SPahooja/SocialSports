package com.uwcs446.socialsports.ui.matchdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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

    private val isHost: Boolean = false // TODO: set boolean

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMatchDetailsBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            binding.matchProgressBar.visibility = VISIBLE
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
                    binding.matchTitle.setText(match.title)
                    binding.matchSportIcon.setImageResource(match.sport.imageResource)
                    binding.matchType.setText(match.sport.toString())
                    binding.matchPlayerCount.setText("${match.currentPlayerCount()} / ${match.maxPlayerCount()}")
                    binding.matchDate.setText(match.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                    binding.matchTime.setText(match.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
                    binding.matchLocationName.setText("High Park") // TODO: add location name field
                    binding.matchAddress.setText("1873 Bloor St W, Toronto, ON M6R 2Z") // TODO: add location address field
                    binding.matchDescription.setText(match.description)
                    binding.matchHostName.setText(host.name)

                    // Get rid of the progress bar
                    binding.matchProgressBar.visibility = GONE

                    // Make everything visible
                    binding.matchTitle.visibility = VISIBLE
                    binding.matchSportIcon.visibility = VISIBLE
                    binding.matchType.visibility = VISIBLE
                    binding.matchPlayerCountIcon.visibility = VISIBLE
                    binding.matchPlayerCount.visibility = VISIBLE
                    binding.matchDate.visibility = VISIBLE
                    binding.matchTime.visibility = VISIBLE
                    binding.matchLocationName.visibility = VISIBLE
                    binding.matchAddress.visibility = VISIBLE
                    binding.matchDescription.visibility = VISIBLE
                    binding.matchHostIcon.visibility = VISIBLE
                    binding.matchHostName.visibility = VISIBLE
                    binding.matchHostLabel.visibility = VISIBLE

                    // TODO get the team from firestore
                    // set up recycler view for two teams
                    val teamSize = match.sport.teamSize
                    // TODO: replace mocks - get player name or some other identifier from match.teamOne and match.teamTwo
                    val teamOne = listOf("John Smith", "John Smith", "John Smith")
                    val teamTwo = listOf("John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith")
                }
            }
        )

        // display the edit button if current user is the match host
        if (isHost) {
            binding.editButtonMatchDetails.visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
