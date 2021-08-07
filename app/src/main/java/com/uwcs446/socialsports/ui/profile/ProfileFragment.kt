package com.uwcs446.socialsports.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.uwcs446.socialsports.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.logoutButton.setOnClickListener { viewModel.handleLogout() }

        val userNameView: TextView = binding.userProfileUsername

        viewModel.username.observe(
            viewLifecycleOwner,
            {
                userNameView.text = it
            }
        )

        val ratingBarView: RatingBar = binding.userProfileRating

        viewModel.rating.observe(
            viewLifecycleOwner,
            {
                ratingBarView.rating = it
            }
        )

        val numRatingsView: TextView = binding.userProfileNumberOfRatings
        viewModel.numRatings.observe(
            viewLifecycleOwner,
            {
                numRatingsView.text = it.toString()
            }
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
