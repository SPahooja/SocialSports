package com.uwcs446.socialsports.ui.find

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.FragmentFindBinding
import com.uwcs446.socialsports.ui.gamelist.MatchRecyclerViewAdapter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class FindFragment : Fragment() {

    private val findViewModel: FindViewModel by viewModels()
    private var _binding: FragmentFindBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        _binding = FragmentFindBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // set up recycler view for match list
        binding.layoutMatchList.recyclerviewMatch.setHasFixedSize(true)
        binding.layoutMatchList.recyclerviewMatch.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val adapter = findViewModel.matchList.value?.let { MatchRecyclerViewAdapter(it) }
        binding.layoutMatchList.recyclerviewMatch.adapter = adapter

        // set up date and time pickers for filter toolbar
        datepickerSetup(binding.layoutListFilterToolbar.edittextFilterDate, parentFragmentManager)
        timepickerSetup(binding.layoutListFilterToolbar.edittextFilterTime, parentFragmentManager)

        // set up type filtering
        val items = listOf(
            getString(R.string.type_all),
            getString(R.string.type_soccer),
            getString(R.string.type_basketball),
            getString(R.string.type_ultimate_frisbee))

        val typeListAdapter = ArrayAdapter(requireContext(), R.layout.type_filter_list_item, items)
        val autoCompleteTextView = binding.layoutListFilterToolbar.matchTypeDropdown
        (autoCompleteTextView as? AutoCompleteTextView)?.setAdapter(typeListAdapter)

        // TODO: update type filtering menu selection listener
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> findViewModel.filterMatchByType("All")
                1 -> findViewModel.filterMatchByType("SOCCER")
                2 -> findViewModel.filterMatchByType("BASKETBALL")
                else -> findViewModel.filterMatchByType("ULTIMATE")
            }

        }

        // default type is "All"
        autoCompleteTextView.setText(getString(R.string.type_all), false)

        // TODO: update observer which updates recyclerview when match data change
        val gameListRecyclerView: RecyclerView = binding.layoutMatchList.recyclerviewMatch
        findViewModel.matchList.observe(
            viewLifecycleOwner,
            {
                if (gameListRecyclerView.adapter == null) {
                    val recyclerViewAdapter = MatchRecyclerViewAdapter(it)
                    gameListRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                    gameListRecyclerView.adapter = recyclerViewAdapter
                } else {
                    gameListRecyclerView.adapter!!.notifyDataSetChanged()
                }
            }
        )

        return root
    }

    /*
        1. set edit text to display the selected date
        2. filter matches by selected date
        3. Clicking cancel will undo the filtering
     */
    private fun datepickerSetup(edittext: EditText, fragmentManager: FragmentManager) {
        edittext.setOnClickListener {
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setStart(MaterialDatePicker.todayInUtcMilliseconds())

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build()

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
    private fun timepickerSetup(edittext: EditText, fragmentManager: FragmentManager){
        edittext.setOnClickListener{
            val timePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setTitleText("Select time")
                    .build()

            timePicker.addOnPositiveButtonClickListener {
                // set text
                val hour = timePicker.hour
                val minute = timePicker.minute
                val selectedTime = "${if (hour == 0) "00" else hour} : ${if (minute == 0) "00" else minute}"
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