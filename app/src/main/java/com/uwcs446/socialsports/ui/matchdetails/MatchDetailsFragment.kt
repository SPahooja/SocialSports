package com.uwcs446.socialsports.ui.matchdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.databinding.FragmentMatchDetailsBinding
import com.uwcs446.socialsports.ui.matchlist.MatchListUtils
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MatchDetailsFragment : Fragment() {

    private val args: MatchDetailsFragmentArgs by navArgs()
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
        val root: View = binding.root

        // Placeholder find the match that matches the passed in ID
//        val matches = MatchListUtils.genFakeMatchData(args.matchId!!.toInt())
//
//        val match = matches.find { match -> match.id == args.matchId }

        val match = MatchListUtils.genFakeMatchData(1)[0] // TODO: this mock for easy testing, replace it

        // Views
        val titleTextView: TextView = binding.matchTitle
        val matchSportIconImageView = binding.matchSportIcon
        val matchTypeTextView: TextView = binding.matchType
        val playerCountTextView: TextView = binding.matchPlayerCount
        val dateTextView: TextView = binding.matchDate
        val timeTextView: TextView = binding.matchTime
        val locationNameTextView: TextView = binding.matchLocationName
        val addressTextView: TextView = binding.matchAddress
        val descriptionTextView: TextView = binding.matchDescription
        val hostNameTextView: TextView = binding.matchHostName

        if (match != null) {
            titleTextView.setText(match.title)
            matchSportIconImageView.setImageResource(match.sport.imageResource)
            matchTypeTextView.setText(match.sport.toString())
            playerCountTextView.setText("${match.currentPlayerCount()} / ${match.maxPlayerCount()}")
            dateTextView.setText(match.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
            timeTextView.setText(match.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
            locationNameTextView.setText("High Park") // TODO: add location name field
            addressTextView.setText("1873 Bloor St W, Toronto, ON M6R 2Z") // TODO: add location address field
            descriptionTextView.setText(match.description)
            hostNameTextView.setText("John Smith") // TODO: Use names from user
        }

        // set up recycler view for two teams
        val teamSize = match.sport.teamSize
        // TODO: replace mocks - get player name or some other identifier from match.teamOne and match.teamTwo
        val teamOne = listOf("John Smith", "John Smith", "John Smith")
        val teamTwo = listOf("John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith", "John Smith")

        val teamOneViewAdapter = TeamListAdapter(teamOne, teamSize)
        binding.layoutTeamOne.recyclerviewTeam.setHasFixedSize(true)
        binding.layoutTeamOne.recyclerviewTeam.stopNestedScroll()
        binding.layoutTeamOne.recyclerviewTeam.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutTeamOne.recyclerviewTeam.adapter = teamOneViewAdapter

        val teamTwoViewAdapter = TeamListAdapter(teamTwo, teamSize)
        binding.layoutTeamTwo.recyclerviewTeam.setHasFixedSize(true)
        binding.layoutTeamTwo.recyclerviewTeam.stopNestedScroll()
        binding.layoutTeamTwo.recyclerviewTeam.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutTeamTwo.recyclerviewTeam.adapter = teamTwoViewAdapter

        // display the edit button if current user is the match host
        if (isHost) {
            binding.editButtonMatchDetails.visibility = View.VISIBLE
            binding.editButtonMatchDetails.setOnClickListener {
                val action = MatchDetailsFragmentDirections.actionMatchDetailsToNavigationHost(match)
                it.findNavController().navigate(action)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
