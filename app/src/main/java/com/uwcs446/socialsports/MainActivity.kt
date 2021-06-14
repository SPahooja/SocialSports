package com.uwcs446.socialsports

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.IdpResponse.fromResultIntent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.uwcs446.socialsports.databinding.ActivityMainBinding
import com.uwcs446.socialsports.services.user.FirebaseUserLoginService
import com.uwcs446.socialsports.utils.RC_SIGN_IN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    FirebaseAuth.AuthStateListener {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.login(this)
        FirebaseUserLoginService.login(this)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {} // TODO explore further to avoid not signing in

    // TODO CAN USE EITHER AUTHLISTENER OR ACTIVITYRESULT FOR FIRST LOGIN (don't need both)
    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        mainViewModel.handleAuthChange()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                val response = fromResultIntent(data)
                Toast.makeText(this, "Logged-In", Toast.LENGTH_SHORT).show() // TODO
            } else {
                Toast.makeText(this, "U messed up", Toast.LENGTH_SHORT).show() // TODO
            }
        }
    }
}
