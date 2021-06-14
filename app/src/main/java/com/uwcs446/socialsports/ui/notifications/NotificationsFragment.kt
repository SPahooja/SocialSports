package com.uwcs446.socialsports.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.uwcs446.socialsports.databinding.FragmentNotificationsBinding
import com.uwcs446.socialsports.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.logoutButton.setOnClickListener { notificationsViewModel.handleLogout() }
        binding.loginButton.setOnClickListener { notificationsViewModel.handleLogin(requireActivity()) }

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(
            viewLifecycleOwner,
            {
                textView.text = it
            }
        )
        val res = notificationsViewModel.testResource()
        when (res) {
            is Resource.Error -> TODO()
            is Resource.Loading -> TODO()
            is Resource.Success -> TODO()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
