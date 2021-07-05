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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.FragmentHostEditDetailsBinding
import com.uwcs446.socialsports.services.match.MatchType
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
        val capacityTextView: EditText = binding.hostInputParticipantCount
        val rulesTextView: EditText = binding.hostInputDescription
        val hostGameButton: Button = binding.hostButtonHost

        // Location and match details of saved context
        locationTitleTextView.text = hostDetailsViewModel.locationTitle
        locationAddressTextView.text = hostDetailsViewModel.locationAddress
        titleTextView.setText(hostDetailsViewModel.matchTitle)
        sportTextView.setText(hostDetailsViewModel.sportType.toString())
        dateTextView.setText(hostDetailsViewModel.matchDate)
        timeTextView.setText(hostDetailsViewModel.matchTime)
        capacityTextView.setText(hostDetailsViewModel.matchCapacity.toString())
        rulesTextView.setText(hostDetailsViewModel.matchDescription)

        // Update user input
        titleTextView.addTextChangedListener {
            hostDetailsViewModel.matchTitle = it.toString()
            titleTextView.error = null
        }

        sportTextView.addTextChangedListener {
            hostDetailsViewModel.sportType = it.toString()
            sportTextView.error = null
        }

        dateTextView.addTextChangedListener {
            hostDetailsViewModel.matchDate = it.toString()
            dateTextView.error = null
        }
        timeTextView.addTextChangedListener {
            hostDetailsViewModel.matchTime = it.toString()
            timeTextView.error = null
        }
        capacityTextView.addTextChangedListener {
            hostDetailsViewModel.matchCapacity = it.toString()
            capacityTextView.error = null
        }
        rulesTextView.addTextChangedListener {
            hostDetailsViewModel.matchDescription = it.toString()
        }

        // Populate sport type list
        val typeListAdapter =
            ArrayAdapter(requireContext(), R.layout.sport_type_item, MatchType.values())
        sportTextView.setAdapter(typeListAdapter)

        // Setup date picker
        dateTextView.setOnClickListener {
            showDatePicker()
        }

        // Setup time picker
        timeTextView.setOnClickListener {
            showTimePicker()
        }

        // Click HostLocation card on host details fragment to edit selected location
        locationCard.setOnClickListener {
            val action = HostDetailsFragmentDirections.actionHostEditDetailsToNavigationHost()
            Navigation.findNavController(requireView()).navigate(action)
        }

        // Host Game Button
        hostGameButton.setOnClickListener {
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
            if (capacityTextView.text.isBlank()) {
                capacityTextView.error = "Please specify number of participants"
                return@setOnClickListener
            }
            hostDetailsViewModel.onSaveClick()
        }

        return root
    }

    private fun showDatePicker() {
        val constraintBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintBuilder)
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val date = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
            val formattedDate = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
            binding.hostInputGameDate.setText(formattedDate)
        }
        datePicker.show(parentFragmentManager, "datePicker")
    }

    private fun showTimePicker() {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select Time")
                .build()

        timePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            val hour: Int = timePicker.hour
            val minute: Int = timePicker.minute
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val viewFormatter = SimpleDateFormat("HH:mm")
            var viewFormattedTime = viewFormatter.format(calendar.time)
            binding.hostInputGameTime.setText(viewFormattedTime)
        }
        timePicker.show(parentFragmentManager, "timePicker")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}