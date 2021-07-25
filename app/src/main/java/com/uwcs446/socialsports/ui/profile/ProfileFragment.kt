package com.uwcs446.socialsports.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.databinding.FragmentProfileBinding
import com.uwcs446.socialsports.services.LocationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null

    @Inject
    lateinit var location: LocationService

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.logoutButton.setOnClickListener { viewModel.handleLogout() }

        val textView: TextView = binding.userProfileUsername
        viewModel.username.observe(
            viewLifecycleOwner,
            {
                textView.text = it
            }
        )

        val ratingBarView: RatingBar = binding.userProfileRating
        viewModel.rating.observe(
            viewLifecycleOwner,
            {
                ratingBarView.rating = it
            }
        )

        println(
            location.getAddress(
                LatLng(
                    43.4723,
                    -80.5449
                )
            )
        ) // UWATERLOO Address (example to get info about an address)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
