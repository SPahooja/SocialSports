package com.uwcs446.socialsports

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.uwcs446.socialsports.databinding.ActivityMainBinding
import com.uwcs446.socialsports.services.user.UserLoginService
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
