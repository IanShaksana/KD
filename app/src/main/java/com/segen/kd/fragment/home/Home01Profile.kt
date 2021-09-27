package com.segen.kd.fragment.home

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
import com.segen.kd.R
import com.segen.kd.databinding.Frag11ProfileBinding
import com.segen.kd.modelbody.AttModelUser
import com.segen.kd.utility.GentUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject
import timber.log.Timber
import java.util.*

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
        binding.demo.setOnClickListener {
            val action =
                Home01ProfileDirections.actionNavHomeToCollectionDemoFragment()
            binding.root.findNavController().navigate(action)
        }
        return binding.root
    }

    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@Home01Profile.requireActivity().getSharedPreferences(
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
                    this@Home01Profile.resources.getString(R.string.homeStat),
                    obj,
                    this@Home01Profile.requireContext()
                )
            )

        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            Timber.i(resp.toString())
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