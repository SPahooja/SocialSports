package com.uwcs446.socialsports.ui.find

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.FragmentFindBinding
import com.uwcs446.socialsports.domain.datetimepicker.DateTimePicker
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.ui.matchlist.MatchRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class FindFragment : Fragment() {

    private val findViewModel: FindViewModel by viewModels()
    private var _binding: FragmentFindBinding? = null

    private val binding get() = _binding!!

    private val recyclerViewData = mutableListOf<Match>()
    private val recyclerViewAdapter = MatchRecyclerViewAdapter(recyclerViewData)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFindBinding.inflate(inflater, container, false)

        // set up recycler view for match list
        binding.layoutMatchList.recyclerviewMatch.setHasFixedSize(true)
        binding.layoutMatchList.recyclerviewMatch.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutMatchList.recyclerviewMatch.adapter = recyclerViewAdapter

        // Observer which updates the recyclerview when match data changes
        findViewModel.matches.observe(viewLifecycleOwner) { matchList ->
            recyclerViewData.clear()
            recyclerViewData.addAll(matchList)
            recyclerViewAdapter.notifyDataSetChanged()
        }

        // set up date and time pickers for filter toolbar
        datepickerSetup(binding.layoutListFilterToolbar.edittextFilterDate, parentFragmentManager)
        timepickerSetup(binding.layoutListFilterToolbar.edittextFilterTime, parentFragmentManager)

        // set up type filtering
        val items = listOf(
            getString(R.string.type_all),
            getString(R.string.type_soccer),
            getString(R.string.type_basketball),
            getString(R.string.type_ultimate_frisbee)
        )

        val typeListAdapter = ArrayAdapter(requireContext(), R.layout.type_filter_list_item, items)
        val autoCompleteTextView = binding.layoutListFilterToolbar.matchTypeDropdown
        (autoCompleteTextView as? AutoCompleteTextView)?.setAdapter(typeListAdapter)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            lifecycleScope.launch {
                when (position) {
                    0 -> findViewModel.filterMatchBySport(Sport.ANY)
                    1 -> findViewModel.filterMatchBySport(Sport.SOCCER)
                    2 -> findViewModel.filterMatchBySport(Sport.BASKETBALL)
                    else -> findViewModel.filterMatchBySport(Sport.ULTIMATE)
                }
            }
        }

        // Default sport is "All"
        autoCompleteTextView.setText(getString(R.string.type_all), false)

        return binding.root
    }

    /*
        1. set edit text to display the selected date
        2. filter matches by selected date
        3. Clicking cancel will undo the filtering
     */
    private fun datepickerSetup(edittext: EditText, fragmentManager: FragmentManager) {
        edittext.setOnClickListener {
            val datePicker = DateTimePicker().getDatePicker()

            datePicker.addOnPositiveButtonClickListener { time ->
                // display selected date
                val date = Instant.ofEpochMilli(time).atZone(ZoneId.of("UTC")).toLocalDate()
                val format = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                edittext.setText(format)

                // filter
                findViewModel.filterMatchByDate(date)
            }

            datePicker.addOnNegativeButtonClickListener {
                // clear text
                edittext.setText("")

                // remove filter
                findViewModel.filterMatchByDate(null)
            }

            datePicker.show(fragmentManager, "datePicker")
        }
    }

    /*
        1. set edit text to display the selected time
        2. filter matches by selected time
        3. Clicking cancel will undo the filtering
     */
    private fun timepickerSetup(edittext: EditText, fragmentManager: FragmentManager) {
        edittext.setOnClickListener {
            val timePicker = DateTimePicker().getTimePicker()

            timePicker.addOnPositiveButtonClickListener {
                // set text
                val hour = timePicker.hour
                val minute = timePicker.minute
                val selectedTime =
                    "${if (hour == 0) "00" else hour} : ${if (minute == 0) "00" else minute}"
                edittext.setText(selectedTime)

                // filter
                findViewModel.filterMatchByTime(hour, minute)
            }

            timePicker.addOnNegativeButtonClickListener {
                // clear text
                edittext.setText("")

                // remove filter
                findViewModel.filterMatchByTime(-1, -1)
            }
            timePicker.show(fragmentManager, "timePicker")
        }
    }
}
