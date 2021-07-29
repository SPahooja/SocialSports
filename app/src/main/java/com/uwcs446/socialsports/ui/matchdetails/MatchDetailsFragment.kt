package com.uwcs446.socialsports.ui.matchdetails

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.MobileNavigationDirections
import com.uwcs446.socialsports.databinding.FragmentMatchDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class MatchDetailsFragment : Fragment() {

    private val args: MatchDetailsFragmentArgs by navArgs()
    private val matchDetailsViewModel: MatchDetailsViewModel by activityViewModels()

    private var _binding: FragmentMatchDetailsBinding? = null
    private val binding get() = _binding!!

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

        // Open map if permission is granted
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Show the map
                val action = MobileNavigationDirections.actionGlobalToMatchLocationMap()
                Navigation.findNavController(binding.root).navigate(action)
            }
        }

        // Open Maps Fragment for Location
        binding.matchLocation.locationItemWrapper.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Show the map
                val action = MobileNavigationDirections.actionGlobalToMatchLocationMap()
                Navigation.findNavController(binding.root).navigate(action)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        val teamOneViewAdapter = TeamListAdapter()
        binding.layoutTeamOne.recyclerviewTeam.setHasFixedSize(false)
        binding.layoutTeamOne.recyclerviewTeam.stopNestedScroll()
        binding.layoutTeamOne.recyclerviewTeam.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutTeamOne.recyclerviewTeam.adapter = teamOneViewAdapter

        val teamTwoViewAdapter = TeamListAdapter()
        binding.layoutTeamTwo.recyclerviewTeam.setHasFixedSize(false)
        binding.layoutTeamTwo.recyclerviewTeam.stopNestedScroll()
        binding.layoutTeamTwo.recyclerviewTeam.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutTeamTwo.recyclerviewTeam.adapter = teamTwoViewAdapter

        matchDetailsViewModel.ready.observe(
            viewLifecycleOwner,
            { ready ->
                if (ready) {
                    val match = matchDetailsViewModel.match.value
                    val host = matchDetailsViewModel.host.value
                    val teamOne = matchDetailsViewModel.teamOne.value
                    val teamTwo = matchDetailsViewModel.teamTwo.value
                    val isHost = matchDetailsViewModel.isHost.value ?: false

                    if (match != null && host != null) {
                        binding.matchSummary.textMatchTitle.text = match.title
                        binding.matchSummary.icMatchType.setImageResource(match.sport.imageResource)
                        binding.matchSummary.textMatchType.text = match.sport.name
                        binding.matchSummary.textMatchPlayerCount.text =
                            "${match.currentPlayerCount()} / ${match.maxPlayerCount()}"
                        binding.matchSummary.textMatchDate.text =
                            match.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                        binding.matchSummary.textMatchTime.text =
                            match.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                        binding.matchLocation.locationItemTitle.text =
                            "High Park" // TODO: add location name field
                        binding.matchLocation.locationItemAddress.text =
                            "1873 Bloor St W, Toronto, ON M6R 2Z" // TODO: add location address field
                        binding.matchLocation.locationItemDistance.text =
                            "10km" // TODO: add distance
                        binding.matchDescription.text = match.description
                        binding.matchHostName.text = host.name

                        // Set team size
                        val teamSize = match.sport.teamSize
                        teamOneViewAdapter.updateTeamSize(teamSize)
                        teamTwoViewAdapter.updateTeamSize(teamSize)

                        // Pass names from teams one and two to recycle view adapter
                        if (teamOne != null) teamOneViewAdapter.updateTeamMembers(teamOne.map { user -> user.name })
                        if (teamTwo != null) teamTwoViewAdapter.updateTeamMembers(teamTwo.map { user -> user.name })

                        // Switch visibility of views
                        binding.matchProgressBar.visibility = GONE
                        binding.matchDetailsViews.visibility = VISIBLE

                        if (isHost) {
                            // display the edit button if current user is the match host
                            binding.editButtonMatchDetails.visibility = VISIBLE
                            binding.editButtonMatchDetails.setOnClickListener {
                                val action = MatchDetailsFragmentDirections.actionMatchDetailsToNavigationHost(match)
                                it.findNavController().navigate(action)
                            }
                        } else {
                            // need to manually hide the button, setting visibility in xml won't work
                            binding.editButtonMatchDetails.visibility = GONE
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
