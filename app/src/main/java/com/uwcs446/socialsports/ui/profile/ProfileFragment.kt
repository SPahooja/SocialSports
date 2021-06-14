package com.uwcs446.socialsports.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uwcs446.socialsports.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.logoutButton.setOnClickListener { profileViewModel.handleLogout() }
        binding.loginButton.setOnClickListener { profileViewModel.handleLogin(requireActivity()) }

        val textView: TextView = binding.textProfile
        profileViewModel.text.observe(
            viewLifecycleOwner,
            {
                textView.text = it
            }
        )
//        when(profileViewModel.testResource()){
//            is Resource.Error -> TODO()
//            is Resource.Loading -> TODO()
//            is Resource.Success -> TODO()
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
