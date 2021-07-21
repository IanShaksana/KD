package com.example.kd.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.kd.R
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

class DemoActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private lateinit var editText: EditText
    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.text = findViewById(R.id.text)
        this.editText = findViewById(R.id.editext)
        this.btn = findViewById(R.id.btn)

        var dage: LocalDate = DateTime(Date()).toLocalDate()


        btn.setOnClickListener {
            text.setText(editText.text)
            Toast.makeText(this, dage.toString(), Toast.LENGTH_SHORT).show()
            this.startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}