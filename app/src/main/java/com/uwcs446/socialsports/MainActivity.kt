package com.uwcs446.socialsports

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.uwcs446.socialsports.databinding.ActivityMainBinding
import com.uwcs446.socialsports.services.user.UserLoginService
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

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

        UserLoginService.login(this)

        Places.initialize(this, getString(R.string.google_api_key), Locale.CANADA)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_find,
                R.id.navigation_host,
                R.id.navigation_profile
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            if (resultCode == RESULT_OK) {
//                val response = fromResultIntent(data)
//                Toast.makeText(this, "Logged-In", Toast.LENGTH_SHORT).show() // TODO
//            } else {
//                Toast.makeText(this, "U messed up", Toast.LENGTH_SHORT).show() // TODO
//            }
//        }
//    }
}
