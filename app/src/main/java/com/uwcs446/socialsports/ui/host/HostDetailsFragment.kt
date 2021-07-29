package com.uwcs446.socialsports.ui.host

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.FragmentHostEditDetailsBinding
import com.uwcs446.socialsports.domain.datetimepicker.DateTimePicker
import com.uwcs446.socialsports.domain.match.MatchLocation
import com.uwcs446.socialsports.domain.match.Sport
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar

@AndroidEntryPoint
class HostDetailsFragment : Fragment() {
    private val TAG = this::class.simpleName
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
        val locationSearch = binding.hostSearchChooseALocationCardView
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

        // Location search
        var matchLocation: MatchLocation? = null
        val locationSearchFragment = childFragmentManager.findFragmentById(R.id.host_search_choose_a_location)
            as AutocompleteSupportFragment
        locationSearchFragment.setHint(getString(R.string.host_hint_search_location))
        locationSearchFragment.setPlaceFields(listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ID))
        locationSearchFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // println("Place: ${place.name}, ${place.address}, ${place.latLng}, ${place.id}")
                matchLocation = MatchLocation(place.id!!, place.latLng!!)
                locationTitleTextView.text = place.name
                locationAddressTextView.text = place.address
                locationSearch.visibility = View.INVISIBLE
                locationCard.visibility = View.VISIBLE
            }
            override fun onError(status: Status) {
                Log.e(TAG, "Something went wrong while searching for places. Status code: $status")
            }
        })

        // Location and match details of saved context
        hostDetailsViewModel.editMatchFlow.observe(viewLifecycleOwner) {
            if (it) {
                locationSearch.visibility = View.INVISIBLE
                locationCard.visibility = View.VISIBLE
            }
        }
        hostDetailsViewModel.locationName.observe(viewLifecycleOwner) {
            locationTitleTextView.text = it
        }
        hostDetailsViewModel.locationAddress.observe(viewLifecycleOwner) {
            locationAddressTextView.text = it
        }
        val matchStartTime = hostDetailsViewModel.matchStartTime
        val matchEndTime = hostDetailsViewModel.matchEndTime

        titleTextView.setText(hostDetailsViewModel.matchTitle)
        sportTextView.setText(hostDetailsViewModel.sportType.toString())
        rulesTextView.setText(hostDetailsViewModel.matchDescription)

        dateTextView.setText(matchStartTime?.atZone(ZoneId.systemDefault())?.format(
            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
        timeTextView.setText(matchStartTime?.atZone(ZoneId.systemDefault())?.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))

        var duration: Duration
        if (matchStartTime != null && matchEndTime != null) {
            duration = Duration.between(matchStartTime, matchEndTime)
        } else {
            duration = Duration.ZERO
        }
        durationHourTextView.setText(duration.toHours().toString())
        durationMinuteTextView.setText((duration.toMinutes() - duration.toHours() * 60).toString())

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
                    date.format(DateTimePicker().getDateFormatter())
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

        // Click MatchLocation card on host details fragment to edit selected location
        locationCard.setOnClickListener {
            locationSearch.visibility = View.VISIBLE
            locationCard.visibility = View.INVISIBLE
        }

        // Host Game Button
        hostGameButton.setOnClickListener {
            if (locationCard.isInvisible) {
                locationSearchFragment.setHint("Location is required")
                return@setOnClickListener
            }
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

            val startTime = LocalDate.parse("${dateTextView.text} ${timeTextView.text}", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).atStartOfDay(
                ZoneId.systemDefault()
            ).toInstant()
            val duration = Duration.ofHours(durationHourTextView.text.toString().toLong()).plus(
                Duration.ofMinutes(durationMinuteTextView.text.toString().toLong())
            )
            val endTime = startTime.plus(duration)

            // Update user input in view model
            if (matchLocation != null) hostDetailsViewModel.matchLocation = matchLocation as MatchLocation
            hostDetailsViewModel.matchTitle = titleTextView.text.toString()
            hostDetailsViewModel.sportType = sportTextView.text.toString()
            hostDetailsViewModel.matchStartTime = startTime
            hostDetailsViewModel.matchEndTime = endTime
            hostDetailsViewModel.matchDescription = rulesTextView.text.toString()

            // Handle click in view model
            hostDetailsViewModel.onSaveClick()

            val editMatchFlow = hostDetailsViewModel.editMatchFlow.value ?: false
            if (editMatchFlow) {
                // return to previous page (which is the details page) when finishing the editing flow
                requireActivity().onBackPressed()
            } else {
                // return to home page when finishing the hosting flow
                Navigation.findNavController(requireView())
                    .navigate(R.id.navigation_home)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
