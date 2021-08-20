package com.example.kd.fragment.home

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.Frag11ProfileBinding
import com.example.kd.dialog.DialogDate
import com.example.kd.modelbody.AttModelUser
import com.example.kd.modelbody.IdOnly
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class Home01Profile : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: Frag11ProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Frag11ProfileBinding.inflate(inflater, container, false)
        background()

        binding.taskCard.setOnClickListener {
            val action =
                Home01ProfileDirections.actionNavHomeToNavCollection()
            binding.root.findNavController().navigate(action)
        }

        binding.attCard.setOnClickListener {
            val action =
                Home01ProfileDirections.actionNavHomeToNavAttendance()
            binding.root.findNavController().navigate(action)
        }

        binding.logoutCard.setOnClickListener {
            val sharedPref = this@Home01Profile.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )
            sharedPref.edit().clear().apply()
            requireActivity().finish()
        }

        return binding.root
    }


    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@Home01Profile.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )
            onSuccess(
                getHome(
                    this@Home01Profile.requireContext().resources.getString(R.string.homeStat),
                    JSONObject(
                        Gson().toJson(
                            AttModelUser(
                                sharedPref.getString(
                                    getString(R.string.loginIdPref),
                                    ""
                                ),
                                Date(),
                                Calendar.getInstance().timeZone.rawOffset
                            )
                        )
                    )
                )
            )
        }
    }

    private fun getHome(url: String, jObject: JSONObject): JSONObject {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this.requireContext())
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        var resp = JSONObject()
        try {
            resp = future.get(15, TimeUnit.SECONDS)
            //val resp = future.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resp

    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            val data = resp.getJSONArray("data").getJSONObject(0)
            binding.apply {
                Timber.i(data.toString())
                nama.text = "Hi, ${data.getString("nama")}"
                posisi.text = "${data.getString("posisi")}"
                cabang.text = "SEGEN ${data.getString("cabang")}"
                countCollection.text = "Tugas Collection : ${data.getString("countCollection")}"
                countPersonal.text = "Tugas Personal : ${data.getString("countPersonal")}"
                if (!data.getBoolean("attendance")) {
                    attendance.text = getString(R.string.label_att_false)
                    attendance.isClickable = true
                } else {
                    attendance.text = getString(R.string.label_att_true)
                    attendance.isClickable = false
                }
            }


        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Toast.makeText(requireContext(), "yuhuuuu", Toast.LENGTH_SHORT).show()
    }


}