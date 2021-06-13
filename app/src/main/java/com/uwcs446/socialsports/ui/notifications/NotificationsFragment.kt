package com.uwcs446.socialsports.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uwcs446.socialsports.databinding.FragmentNotificationsBinding
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
    ): View? {
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
//        activity?.let {
//            AuthUI.getInstance().signOut(it)
//        } // TODO remove and add dedicated ui for logout
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
