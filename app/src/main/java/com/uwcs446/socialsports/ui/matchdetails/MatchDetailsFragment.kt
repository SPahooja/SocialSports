package com.uwcs446.socialsports.ui.matchdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.uwcs446.socialsports.databinding.FragmentMatchDetailsBinding
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MatchDetailsFragment : Fragment() {

    private val matchDetailsViewModel: MatchDetailsViewModel by viewModels()
    private val args: MatchDetailsFragmentArgs by navArgs()
    private var _binding: FragmentMatchDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMatchDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        matchDetailsViewModel.findMatchById(args.matchId!!)

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

        titleTextView.text = matchDetailsViewModel.match.value?.title
        matchDetailsViewModel.match.value?.sport?.let { matchSportIconImageView.setImageResource(it.imageResource) }
        matchTypeTextView.text = matchDetailsViewModel.match.value?.sport.toString()
        playerCountTextView.text = "${matchDetailsViewModel.match.value?.currentPlayerCount()} / ${matchDetailsViewModel.match.value?.maxPlayerCount()}"
        dateTextView.text = matchDetailsViewModel.match.value?.date?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        timeTextView.text = matchDetailsViewModel.match.value?.time?.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        locationNameTextView.text = "High Park" // TODO: add location name field
        addressTextView.text = "1873 Bloor St W, Toronto, ON M6R 2Z" // TODO: add location address field
        descriptionTextView.text = matchDetailsViewModel.match.value?.description
        hostNameTextView.text = "John Smith" // TODO: Use names from user

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
