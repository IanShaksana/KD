package com.example.kd.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kd.R
import com.google.android.material.navigation.NavigationView
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navigation: NavigationView
    private lateinit var drawerlayout: DrawerLayout

    private lateinit var text: TextView
    private lateinit var editText: EditText
    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawer_process_2()

        this.text = findViewById(R.id.text)
        this.editText = findViewById(R.id.editext)
        this.btn = findViewById(R.id.btn)
        var date: LocalDate = DateTime(Date()).toLocalDate()
        btn.setOnClickListener {
            text.setText(editText.text)
            Toast.makeText(this, date.toString(), Toast.LENGTH_SHORT).show()
            this.startActivity(Intent(this, LoginActivity::class.java))

            finish()

        }

    }

    private fun frag_tran(selectedFragment: Fragment?) {
        if (selectedFragment != null) {
            // getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).replace(R.id.main_fragment, selectedFragment).commit();
            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, selectedFragment)
                .commit()
        }
    }

    private fun drawer_process() {
        navigation = findViewById(R.id.nav_view)
        drawerlayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration.Builder().setOpenableLayout(drawerlayout).build()
        navigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "nav home", Toast.LENGTH_SHORT).show();
                    true
                }
                R.id.nav_achievement -> {
                    Toast.makeText(this, "nav gallery", Toast.LENGTH_SHORT).show();
                    true
                }
                R.id.nav_attendance -> {
                    Toast.makeText(this, "nav slideshow", Toast.LENGTH_SHORT).show();
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this, "nav slideshow", Toast.LENGTH_SHORT).show();
                    true
                }
                else -> false
            }

        }
    }

    private fun drawer_process_2() {

        navController = findNavController(R.id.main_fragment)
        drawerlayout = findViewById(R.id.drawer_layout)
        navigation = findViewById(R.id.nav_view)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerlayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigation.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}