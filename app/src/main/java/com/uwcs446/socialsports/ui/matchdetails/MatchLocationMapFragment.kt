package com.uwcs446.socialsports.ui.matchdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.uwcs446.socialsports.R

class MatchLocationMapFragment : Fragment() {

    private lateinit var map: GoogleMap

    private val matchDetailsViewModel: MatchDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_location_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val place = matchDetailsViewModel.matchPlace.value!!

        Log.d(this::class.simpleName, place.toString())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            googleMap.addMarker(
                MarkerOptions()
                    .position(place.latLng)
                    .title(place.name)
                    .snippet(place.address)
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}
