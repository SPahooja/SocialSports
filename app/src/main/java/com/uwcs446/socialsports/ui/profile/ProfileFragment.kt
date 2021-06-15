package com.uwcs446.socialsports.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.uwcs446.socialsports.databinding.FragmentProfileBinding
import com.uwcs446.socialsports.services.user.UserLoginService
import com.uwcs446.socialsports.utils.Resource
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
        binding.loginButton.setOnClickListener { UserLoginService.login(requireActivity()) }

        val textView: TextView = binding.textProfile
        viewModel.text.observe(
            viewLifecycleOwner,
            {
                textView.text = it
            }
        )
        println(
            "Resource is ${
            when (viewModel.testResource()) {
                is Resource.Error -> "in Error"
                is Resource.Loading -> "Loading"
                is Resource.Success -> "Done"
            }
            }"
        )
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}