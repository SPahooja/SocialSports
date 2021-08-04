package com.uwcs446.socialsports.ui.find

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Filter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.FragmentFindBinding
import com.uwcs446.socialsports.domain.datetimepicker.DateTimePicker
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.ui.matchlist.MatchListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

@AndroidEntryPoint
class FindFragment : Fragment() {

    private val findViewModel: FindViewModel by viewModels()
    private var _binding: FragmentFindBinding? = null

    private val binding get() = _binding!!

    private val recyclerViewAdapter = MatchListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFindBinding.inflate(inflater, container, false)

        binding.findProgressBar.visibility = VISIBLE
        binding.layoutMatchList.recyclerviewMatch.visibility = INVISIBLE

        // set up recycler view for match list
        binding.layoutMatchList.recyclerviewMatch.setHasFixedSize(true)
        binding.layoutMatchList.recyclerviewMatch.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.layoutMatchList.recyclerviewMatch.adapter = recyclerViewAdapter

        // Observer which updates the recyclerview when match data changes
        findViewModel.matchPlaces.observe(viewLifecycleOwner) { matchPlaces ->
            recyclerViewAdapter.updateMatchList(matchPlaces)

            binding.findProgressBar.visibility = INVISIBLE
            binding.layoutMatchList.recyclerviewMatch.visibility = VISIBLE
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

        val typeListAdapter = NoFilterAdapter(requireContext(), R.layout.type_filter_list_item, items)
        val autoCompleteTextView = binding.layoutListFilterToolbar.matchTypeDropdown
        autoCompleteTextView.freezesText = true
        (autoCompleteTextView as? AutoCompleteTextView)?.setAdapter(typeListAdapter)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            binding.findProgressBar.visibility = VISIBLE
            binding.layoutMatchList.recyclerviewMatch.visibility = INVISIBLE

            when (position) {
                0 -> findViewModel.filterType.value = Sport.ANY
                1 -> findViewModel.filterType.value = Sport.SOCCER
                2 -> findViewModel.filterType.value = Sport.BASKETBALL
                else -> findViewModel.filterType.value = Sport.ULTIMATE
            }
        }

        // Default sport is "All"
        autoCompleteTextView.setText(getString(R.string.type_all), false)

        // set up observers for filter options
        findViewModel.filterDate.observe(viewLifecycleOwner) {
            filterProcess()
        }

        findViewModel.filterTime.observe(viewLifecycleOwner) {
            filterProcess()
        }

        findViewModel.filterType.observe(viewLifecycleOwner) {
            filterProcess()
        }

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
                val format = date.format(DateTimePicker().getDateFormatter())
                edittext.setText(format)

                // filter
                findViewModel.filterDate.value = format
            }

            datePicker.addOnNegativeButtonClickListener {
                // clear text
                edittext.setText("")

                // remove filter
                findViewModel.filterDate.value = ""
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
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                val viewFormatter = SimpleDateFormat("HH:mm")
                var formattedTime = viewFormatter.format(calendar.time)

                edittext.setText(formattedTime)

                // filter
                findViewModel.filterTime.value = formattedTime
            }

            timePicker.addOnNegativeButtonClickListener {
                // clear text
                edittext.setText("")

                // remove filter
                findViewModel.filterTime.value = ""
            }
            timePicker.show(fragmentManager, "timePicker")
        }
    }

    private fun filterProcess() {
        lifecycleScope.launch {
            binding.findProgressBar.visibility = VISIBLE
            binding.layoutMatchList.recyclerviewMatch.visibility = INVISIBLE
            findViewModel.filterMatch()
        }
    }

    // override ArrayAdapter filter to avoid spinner options disappearing when navigating back from details page
    class NoFilterAdapter(context: Context, layout: Int, val items: List<String>) :
        ArrayAdapter<String>(context, layout, items) {

        private val noOpFilter = object : Filter() {
            private val noOpResult = FilterResults()
            override fun performFiltering(constraint: CharSequence?) = noOpResult
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
        }

        override fun getFilter() = noOpFilter
    }
}
