package com.uwcs446.socialsports.ui.host.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uwcs446.socialsports.databinding.FragmentHostLocationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostLocationFragment : Fragment() {

    private val hostLocationViewModel: HostLocationViewModel by viewModels()
    private var _binding: FragmentHostLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentHostLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Views
        val searchView: SearchView = binding.hostSearchChooseALocation
        val suggestionsRecyclerView: RecyclerView = binding.hostLocationSuggestions
        val searchResultTextView: TextView = binding.hostTextLocationSearchResult
        val searchResultRecyclerView: RecyclerView = binding.hostLocationSearchResult

        // Location suggestions
        val suggestionsAdapter = LocationListAdapter()
        suggestionsRecyclerView.apply {
            adapter = suggestionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        hostLocationViewModel.hostLocationSuggestions.observe(
            viewLifecycleOwner,
            {
                it?.let {
                    suggestionsAdapter.submitList(it)
                }
            }
        )

        // Location search results
        val searchResultsAdapter = LocationListAdapter()
        searchResultRecyclerView.apply {
            adapter = searchResultsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        hostLocationViewModel.hostLocationSearchResults.observe(
            viewLifecycleOwner,
            {
                it?.let {
                    searchResultsAdapter.submitList(it)
                }
            }
        )

        // Location search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hostLocationViewModel.getLocationSearchResults(query)
                searchResultTextView.visibility = View.VISIBLE
                searchResultRecyclerView.visibility = View.VISIBLE
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
