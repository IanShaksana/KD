package com.example.kd.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kd.R
import com.example.kd.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navigation: NavigationView
    private lateinit var drawerLayout: DrawerLayout


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerProcess()
    }

    private fun drawerProcess() {

        navController = findNavController(R.id.main_fragment)

        drawerLayout = binding.drawerLayout
        navigation = binding.navView
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_attendance,
                R.id.nav_achievement,
                R.id.nav_history,
                R.id.nav_collection,
                R.id.nav_personal,
                R.id.nav_deposito_submit,
                R.id.nav_loan_submit,
            ), drawerLayout
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        navigation.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}