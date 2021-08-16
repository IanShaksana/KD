package com.example.kd.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kd.R
import com.example.kd.databinding.ActivityMain2Binding
import com.example.kd.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navigation: NavigationView
    private lateinit var drawerLayout: DrawerLayout


    private lateinit var binding: ActivityMainBinding
    private lateinit var binding2: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = this@MainActivity.getSharedPreferences(
            getString(R.string.credPref), Context.MODE_PRIVATE
        )


        if (!sharedPref.getString(getString(R.string.posisiPref), "").equals("manager", true)) {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            drawerProcess()
        } else {
            binding2 = ActivityMain2Binding.inflate(layoutInflater)
            setContentView(binding2.root)
            drawerProcess2()

        }
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
                R.id.nav_collection,
                R.id.nav_deposito_submit,
                R.id.nav_loan_submit,
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigation.setupWithNavController(navController)

    }

    private fun drawerProcess2() {

        navController = findNavController(R.id.main_fragment)

        drawerLayout = binding2.drawerLayout
        navigation = binding2.navView
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_attendance,
                R.id.nav_task_manager, // ini diganti
                R.id.nav_deposito_submit, // ini diganti
                R.id.nav_loan_submit, // ini diganti
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