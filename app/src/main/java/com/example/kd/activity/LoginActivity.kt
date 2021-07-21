package com.example.kd.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.kd.R

class LoginActivity : AppCompatActivity() {

    private lateinit var login: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        login = findViewById(R.id.log_in)
        login.setOnClickListener {
            this.startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}