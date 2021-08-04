package com.uwcs446.socialsports

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.launch
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

        createNotificationChannel()

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

        // Check for location permission
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // TODO CAN USE EITHER AUTHLISTENER OR ACTIVITYRESULT FOR FIRST LOGIN (don't need both)
    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        mainViewModel.handleAuthChange()

        // This forces the user to log in, so that they can't use the app logged out.
        UserLoginService.login(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle sign in activity completion
        if (requestCode == RC_SIGN_IN) {
            UserLoginService.isLoginActivityActive = false

            // Retry login if user did not log in successfully
            if (resultCode != RESULT_OK) {
                UserLoginService.login(this)
            }
        }
    }

    // Creates notification channel with channel id, name, description, importance, and registers the
    // channel to a NotificationManager in the system.
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.match_rating_notification_channel_name)
            val descriptionText = getString(R.string.match_rating_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.match_rating_notification_channel_id), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Support back button in action bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
