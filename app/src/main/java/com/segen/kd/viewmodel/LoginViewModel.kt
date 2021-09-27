package com.segen.kd.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segen.kd.R
import com.segen.kd.background.HttpCon
import com.segen.kd.modelbody.LoginModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber


class LoginViewModel : ViewModel() {
    var email: String = ""
    var pass: String = ""
    var status: Boolean = false

    init {
        Timber.i("LoginViewModel Created")
    }

    fun login(context: Context) {
        // Create a new coroutine to move the execution off the UI thread
        viewModelScope.launch {
            val cred = LoginModel(email, pass)
            val jObject = JSONObject(Gson().toJson(cred))

            val resp = HttpCon(context).makePostRequest(
                context.resources.getString(R.string.login),
                jObject
            )
            if ((resp?.get("status") ?: 0) == 1) {
                status = true
            }
        }


    }
}