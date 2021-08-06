package com.uwcs446.socialsports.ui.matchdetails

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.uwcs446.socialsports.receivers.RateReminderBroadcast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
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
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // Show the map
                    val action = MobileNavigationDirections.actionGlobalToMatchLocationMap()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }

        // Open Maps Fragment for Location
        binding.matchLocation.locationItemWrapper.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
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
        binding.layoutTeamOne.recyclerviewTeam.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutTeamOne.recyclerviewTeam.adapter = teamOneViewAdapter

        val teamTwoViewAdapter = TeamListAdapter()
        binding.layoutTeamTwo.recyclerviewTeam.setHasFixedSize(false)
        binding.layoutTeamTwo.recyclerviewTeam.stopNestedScroll()
        binding.layoutTeamTwo.recyclerviewTeam.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutTeamTwo.recyclerviewTeam.adapter = teamTwoViewAdapter

        matchDetailsViewModel.ready.observe(
            viewLifecycleOwner,
            { ready ->
                if (ready) {
                    val match = matchDetailsViewModel.match.value
                    val place = matchDetailsViewModel.place.value
                    val host = matchDetailsViewModel.host.value
                    val teamOne = matchDetailsViewModel.teamOne.value
                    val teamTwo = matchDetailsViewModel.teamTwo.value
                    val isHost = matchDetailsViewModel.isHost.value ?: false

                    if (match != null && host != null && place != null) {
                        // time the match ends
                        val endTimeInMillis = match.endTime.toEpochMilli()
                        // Using this callback since to reduce code duplication. Will be called via all
                        //  the onClickListens for the join buttons
                        val rateReminderCallback = { matchId: String?, endTime: Long ->
                            val intent = Intent(context, RateReminderBroadcast::class.java)
                            // Passing the matchId so the MatchRatingActivity will know the match it is dealing with
                            intent.putExtra("MatchId", matchId)
                            // Pending intent, will trigger the RateReminderBroadcast
                            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

                            // Using alarm manager to trigger a notification via broadcast receiver after the game is done
                            alarmManager?.set(
                                AlarmManager.RTC_WAKEUP,
                                endTime,
                                pendingIntent
                            )
                        }

                        binding.matchSummary.textMatchTitle.text = match.title
                        binding.matchSummary.icMatchType.setImageResource(match.sport.imageResource)
                        binding.matchSummary.textMatchType.text = match.sport.name
                        binding.matchSummary.textMatchPlayerCount.text =
                            "${match.currentPlayerCount()} / ${match.maxPlayerCount()}"
                        binding.matchSummary.textMatchDate.text =
                            match.startTime.atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                        binding.matchSummary.textMatchTime.text =
                            match.startTime.atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                        binding.matchLocation.locationItemTitle.text =
                            place.name // TODO: add location name field
                        binding.matchLocation.locationItemAddress.text =
                            place.address // TODO: add location address field
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

                        // Enable or disable join buttons
                        if (matchDetailsViewModel.isInTeam(1)) {
                            // Enable leave team one button
                            binding.matchTeamOneJoinButton.text = "Leave"
                            binding.matchTeamOneJoinButton.isEnabled = true
                            binding.matchTeamOneJoinButton.setOnClickListener {
                                matchDetailsViewModel.leaveMatch(
                                    1
                                )
                            }
                            // Disable join team two button
                            binding.matchTeamTwoJoinButton.text = "Join"
                            binding.matchTeamTwoJoinButton.isEnabled = false
                            binding.matchTeamTwoJoinButton.setOnClickListener {
                                matchDetailsViewModel.joinMatch(
                                    2
                                )
                            }
                        } else if (matchDetailsViewModel.isInTeam(2)) {
                            // Disable join team one button
                            binding.matchTeamOneJoinButton.text = "Join"
                            binding.matchTeamOneJoinButton.isEnabled = false
                            binding.matchTeamOneJoinButton.setOnClickListener {
                                matchDetailsViewModel.joinMatch(
                                    1
                                )
                            }
                            // Enable leave team two button
                            binding.matchTeamTwoJoinButton.text = "Leave"
                            binding.matchTeamTwoJoinButton.isEnabled = true
                            binding.matchTeamTwoJoinButton.setOnClickListener {
                                matchDetailsViewModel.leaveMatch(
                                    2
                                )
                            }
                        } else {
                            // Enable join team one button
                            binding.matchTeamOneJoinButton.text = "Join"
                            binding.matchTeamOneJoinButton.isEnabled = true
                            binding.matchTeamOneJoinButton.setOnClickListener {
                                matchDetailsViewModel.joinMatch(
                                    1
                                )
                                rateReminderCallback(match.id, endTimeInMillis)
                            }
                            // Enable join team two button
                            binding.matchTeamTwoJoinButton.text = "Join"
                            binding.matchTeamTwoJoinButton.isEnabled = true
                            binding.matchTeamTwoJoinButton.setOnClickListener {
                                matchDetailsViewModel.joinMatch(
                                    2
                                )
                                rateReminderCallback(match.id, endTimeInMillis)
                            }
                        }

                        // Switch visibility of views
                        binding.matchProgressBar.visibility = GONE
                        binding.matchDetailsViews.visibility = VISIBLE

                        if (isHost && match.startTime.isAfter(Instant.now())) {
                            // display the edit button if current user is the match host
                            binding.editButtonMatchDetails.visibility = VISIBLE
                            binding.editButtonMatchDetails.setOnClickListener {
                                val action =
                                    MatchDetailsFragmentDirections.actionMatchDetailsToNavigationHost(
                                        match
                                    )
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
