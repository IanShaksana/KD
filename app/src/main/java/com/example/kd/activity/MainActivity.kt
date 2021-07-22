package com.example.kd.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kd.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navigation: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerProcess()
    }



    private fun drawerProcess() {

        val wasd = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        navController = wasd.navController
        //navController = findNavController(R.id.main_fragment)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigation = findViewById(R.id.nav_view)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigation.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val wasd = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        val navController = wasd.navController
        // val navController = findNavController(R.id.main_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}