package com.example.kd.background

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class HttpCon(private val context: Context) {

    private fun volleyGet(url: String): JSONObject {
        val queue = Volley.newRequestQueue(this.context)
        var resp = JSONObject()

        val stringRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Display the first 500 characters of the response string.
                Timber.i("Response is: $response")
                resp = response
            },
            { error ->
                Timber.i("That didn't work!: $error")
            })

        queue.add(stringRequest)
        return resp
    }

    private fun volleyGetFuture(url: String): JSONObject {
        val queue = Volley.newRequestQueue(this.context)
        var resp = JSONObject()

        val stringRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Display the first 500 characters of the response string.
                Timber.i("Response is: $response")
                resp = response
            },
            { error ->
                Timber.i("That didn't work!: $error")
            })

        queue.add(stringRequest)
        return resp
    }

    private fun volleyPost(url: String, jObject: JSONObject): JSONObject {
        val queue = Volley.newRequestQueue(this.context)
        var resp = JSONObject()

        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject,
            { response ->
                // Display the first 500 characters of the response string.
                Timber.i("Response is: $response")
                resp = response
            },
            { error ->
                Timber.i("That didn't work!: $error")
            })

        queue.add(stringRequest)
        return resp
    }

    private fun volleyPostFuture(url: String, jObject: JSONObject): JSONObject? {

        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this.context)
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        try {
            return future.get(5000, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    suspend fun makePostRequest(url: String, jsonBody: JSONObject): JSONObject? {
        // Move the execution of the coroutine to the I/O dispatcher
        return withContext(Dispatchers.IO) {
            return@withContext volleyPostFuture(url, jsonBody)
        }
    }

    suspend fun makeGetRequest(url: String): JSONObject {

        // Move the execution of the coroutine to the I/O dispatcher
        return withContext(Dispatchers.IO) {
            return@withContext volleyGet(url)
        }
    }
}