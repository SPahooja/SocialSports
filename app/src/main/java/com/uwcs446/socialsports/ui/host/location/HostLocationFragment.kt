package com.uwcs446.socialsports.ui.host.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.FragmentHostLocationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostLocationFragment : Fragment() {

    private val hostLocationViewModel: HostLocationViewModel by viewModels()
    private var _binding: FragmentHostLocationBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentHostLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Location suggestions
        val suggestionsRecyclerView: RecyclerView = binding.hostLocationSuggestions
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

        // Location search
        val locationSearchFragment = childFragmentManager.findFragmentById(R.id.host_search_choose_a_location)
            as AutocompleteSupportFragment
        locationSearchFragment.setHint(getString(R.string.host_hint_search_location))
        locationSearchFragment.setPlaceFields(listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ID))
        locationSearchFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // println("Place: ${place.name}, ${place.address}, ${place.latLng}, ${place.id}")
                val action = HostLocationFragmentDirections.actionNavigationHostToHostEditDetails(place.id!!)
                Navigation.findNavController(requireView()).navigate(action)
            }
            override fun onError(status: Status) {
                // TODO: Handle the error.
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
