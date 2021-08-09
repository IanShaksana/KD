package com.example.kd.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.background.HttpCon
import com.example.kd.databinding.LoginLayoutBinding
import com.example.kd.modelbody.LoginModel
import com.example.kd.viewmodel.LoginViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginLayoutBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        dataHold()
        checkLogin()

        binding.logIn.setOnClickListener {
            viewModel.email = binding.emailInput.text.toString()
            viewModel.pass = binding.passInput.text.toString()
            binding.logIn.isEnabled = false

            CoroutineScope(Dispatchers.IO).launch {
                val cred = LoginModel(viewModel.email, viewModel.pass)
                val jObject = JSONObject(Gson().toJson(cred))
                login(this@LoginActivity.resources.getString(R.string.login), jObject)
            }


        }

    }

    private fun dataHold() {
        binding.apply {
            emailInput.setText(viewModel.email)
            passInput.setText(viewModel.pass)
        }
    }

    private suspend fun login(url: String, jObject: JSONObject) {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this)
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        try {
            val resp = future.get(5000, TimeUnit.MILLISECONDS)
            onSuccess(resp)
        } catch (e: Exception) {
            e.printStackTrace()
            binding.logIn.isEnabled = true
        }
    }


    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                val sharedPref = this@LoginActivity.getSharedPreferences(
                    getString(R.string.credPref), Context.MODE_PRIVATE
                )

                val data = resp.getJSONArray("data").getJSONObject(0)
                val editor = sharedPref.edit()
                with(editor) {
                    putString(getString(R.string.loginIdPref), data["id"].toString())
                    apply()
                }

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@LoginActivity, resp["message"].toString(), Toast.LENGTH_SHORT)
                    .show()
            }
            binding.logIn.isEnabled = true
        }
    }

    private fun checkLogin() {
        val sharedPref = this@LoginActivity.getSharedPreferences(
            getString(R.string.credPref), Context.MODE_PRIVATE
        )
        if (!sharedPref.getString(getString(R.string.loginIdPref), "").equals("", true)) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}