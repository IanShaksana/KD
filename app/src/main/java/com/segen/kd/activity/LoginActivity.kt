package com.segen.kd.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.segen.kd.R
import com.segen.kd.databinding.LoginLayoutBinding
import com.segen.kd.modelbody.LoginModel
import com.segen.kd.utility.GentUtil
import com.segen.kd.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

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
            login()
        }

    }


    private fun login() {
        val cred = LoginModel(viewModel.email, viewModel.pass)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                onSuccess(
                    GentUtil().process(
                        this@LoginActivity.resources.getString(R.string.login),
                        cred,
                        this@LoginActivity
                    )
                )
            }catch (e:Exception){
                e.printStackTrace()
                binding.logIn.isEnabled = true
            }

        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                val sharedPref = this@LoginActivity.getSharedPreferences(
                    getString(R.string.credPref), Context.MODE_PRIVATE
                )

                val data = resp.getJSONArray("data").getJSONObject(0)

                Timber.i(data.toString())
                val editor = sharedPref.edit()
                with(editor) {
                    putString(getString(R.string.loginIdPref), data["id"].toString())
                    putString(getString(R.string.posisiPref), data["posisi"].toString())
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


    private fun dataHold() {
        binding.apply {
            emailInput.setText(viewModel.email)
            passInput.setText(viewModel.pass)
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