package com.uwcs446.socialsports.ui.host.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.FragmentHostEditDetailsBinding
import com.uwcs446.socialsports.domain.datetimepicker.DateTimePicker
import com.uwcs446.socialsports.domain.match.Sport
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar

@AndroidEntryPoint
class HostDetailsFragment : Fragment() {

    private val hostDetailsViewModel: HostDetailsViewModel by viewModels()
    private var _binding: FragmentHostEditDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentHostEditDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Views
        val locationCard = binding.hostCardSelectedLocation
        val locationTitleTextView: TextView = binding.hostSelectedLocationTitle
        val locationAddressTextView: TextView = binding.hostSelectedLocationAddress
        val titleTextView: EditText = binding.hostInputGameTitle
        val sportTextView: AutoCompleteTextView = binding.hostInputGameSport
        val dateTextView: EditText = binding.hostInputGameDate
        val timeTextView: EditText = binding.hostInputGameTime
        val durationHourTextView: EditText = binding.hostInputDurationHour
        val durationMinuteTextView: EditText = binding.hostInputDurationMinute
        val rulesTextView: EditText = binding.hostInputDescription
        val hostGameButton: Button = binding.hostButtonHost

        // Location and match details of saved context
        locationTitleTextView.text = hostDetailsViewModel.locationTitle
        locationAddressTextView.text = hostDetailsViewModel.locationAddress
        titleTextView.setText(hostDetailsViewModel.matchTitle)
        sportTextView.setText(hostDetailsViewModel.sportType.toString())
        dateTextView.setText(hostDetailsViewModel.matchDate)
        timeTextView.setText(hostDetailsViewModel.matchTime)
        durationHourTextView.setText(hostDetailsViewModel.matchDurationHour.toString())
        durationMinuteTextView.setText(hostDetailsViewModel.matchDurationMinute.toString())
        rulesTextView.setText(hostDetailsViewModel.matchDescription)

        // Populate sport type list
        val typeListAdapter =
            ArrayAdapter(requireContext(), R.layout.sport_type_item, Sport.values())
        sportTextView.setAdapter(typeListAdapter)

        // Setup date picker
        dateTextView.setOnClickListener {
            val datePicker = DateTimePicker().getDatePicker()
            datePicker.addOnPositiveButtonClickListener {
                val date = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                val formattedDate =
                    date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                dateTextView.setText(formattedDate)
            }
            datePicker.show(parentFragmentManager, "datePicker")
        }

        // Setup time picker
        timeTextView.setOnClickListener {
            val timePicker = DateTimePicker().getTimePicker()
            timePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                calendar.set(Calendar.MINUTE, timePicker.minute)
                val viewFormatter = SimpleDateFormat("HH:mm")
                var formattedTime = viewFormatter.format(calendar.time)
                timeTextView.setText(formattedTime)
            }
            timePicker.show(parentFragmentManager, "timePicker")
        }

        // Click HostLocation card on host details fragment to edit selected location
        locationCard.setOnClickListener {
            val action = HostDetailsFragmentDirections.actionHostEditDetailsToNavigationHost()
            Navigation.findNavController(requireView()).navigate(action)
        }

        // Host Game Button
        hostGameButton.setOnClickListener {
            // Field validation
            if (titleTextView.text.isBlank()) {
                titleTextView.error = "Please enter a game title"
                return@setOnClickListener
            }
            if (sportTextView.text.isBlank()) {
                sportTextView.error = "Please select a sport type"
                return@setOnClickListener
            }
            if (dateTextView.text.isBlank()) {
                dateTextView.error = "Please select a date"
                return@setOnClickListener
            }
            if (timeTextView.text.isBlank()) {
                timeTextView.error = "Please select time"
                return@setOnClickListener
            }
            if (durationHourTextView.text.isBlank() && durationMinuteTextView.text.isBlank()) {
                durationMinuteTextView.error = "Please specify match duration"
                return@setOnClickListener
            }

            // Update user input in view model
            hostDetailsViewModel.matchTitle = titleTextView.text.toString()
            hostDetailsViewModel.sportType = sportTextView.text.toString()
            hostDetailsViewModel.matchDate = dateTextView.text.toString()
            hostDetailsViewModel.matchTime = timeTextView.text.toString()
            hostDetailsViewModel.matchDurationHour = durationHourTextView.text.toString()
            hostDetailsViewModel.matchDurationMinute = durationMinuteTextView.text.toString()
            hostDetailsViewModel.matchDescription = rulesTextView.text.toString()

            // Handle click in view model
            hostDetailsViewModel.onSaveClick()
            Navigation.findNavController(requireView())
                .navigate(R.id.navigation_home) // TODO: for demo
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
