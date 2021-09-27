package com.segen.kd.fragment.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.segen.kd.R
import com.segen.kd.databinding.Frag12AttendanceBinding
import com.segen.kd.modelbody.AttModelUser
import com.segen.kd.utility.GentUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject
import java.util.*

class Home02Attendance : Fragment() {


    private lateinit var binding: Frag12AttendanceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Frag12AttendanceBinding.inflate(inflater, container, false)
        backgroundCheck()
        binding.attendanceBtn.setOnClickListener {
            background()
        }
        binding.attendanceCard.setOnClickListener {
            val action =
                Home02AttendanceDirections.actionNavAttendanceToAttendanceList()
            binding.root.findNavController().navigate(action)
        }

        return binding.root
    }


    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@Home02Attendance.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )
            val obj = AttModelUser(
                sharedPref.getString(
                    getString(R.string.loginIdPref),
                    ""
                ),
                DateTime(Date()).toString("MMM dd, yyyy hh:mm:ss a"),
                Calendar.getInstance().timeZone.rawOffset
            )

            onSuccess(
                GentUtil().process(
                    this@Home02Attendance.resources.getString(R.string.attendancePost),
                    obj,
                    this@Home02Attendance.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                Toast.makeText(
                    requireContext(),
                    "Absen Berhasil",
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.findNavController().popBackStack()
            }
        }
    }

    private fun backgroundCheck() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@Home02Attendance.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )
            val obj = AttModelUser(
                sharedPref.getString(
                    getString(R.string.loginIdPref),
                    ""
                ),
                DateTime(Date()).toString("MMM dd, yyyy hh:mm:ss a"),
                Calendar.getInstance().timeZone.rawOffset
            )

            onSuccessCheck(
                GentUtil().process(
                    this@Home02Attendance.resources.getString(R.string.homeStat),
                    obj,
                    this@Home02Attendance.requireContext()
                )
            )

        }
    }

    private suspend fun onSuccessCheck(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            val data = resp.getJSONArray("data").getJSONObject(0)
            binding.apply {
                attendanceBtn.isEnabled = !data.getBoolean("attendance")
            }


        }
    }

}