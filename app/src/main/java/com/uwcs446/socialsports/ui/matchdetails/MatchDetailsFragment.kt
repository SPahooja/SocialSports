package com.uwcs446.socialsports.ui.matchdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.fragment.navArgs
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.FragmentMatchDetailsBinding
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.ui.matchlist.MatchListUtils
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class MatchDetailsFragment : Fragment() {

    private val args: MatchDetailsFragmentArgs by navArgs()
    private var _binding: FragmentMatchDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MatchDetailsViewModel by viewModels ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMatchDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Placeholder find the match that matches the passed in ID
        val matches = MatchListUtils.genFakeMatchData(args.matchId!!.toInt())

        val match = matches.find { match -> match.id == args.matchId }

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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.match.observe(viewLifecycleOwner) { match ->
            updateViewWithMatch(match)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateViewWithMatch(match: Match) {
        binding.matchTitle.setText(match.title)
        binding.matchSportIcon.setImageResource(match.sport.imageResource)
        binding.matchType.setText(match.sport.toString())
        binding.matchPlayerCount.setText("${match.currentPlayerCount()} / ${match.maxPlayerCount()}")
        binding.matchDate.setText(match.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
        binding.matchTime.setText(match.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
        binding.matchLocationName.setText("High Park") // TODO: add location name field
        binding.matchAddress.setText("1873 Bloor St W, Toronto, ON M6R 2Z") // TODO: add location address field
        binding.matchDescription.setText(match.description)
        binding.matchHostName.setText("John Smith") // TODO: Use names from user
    }
}
