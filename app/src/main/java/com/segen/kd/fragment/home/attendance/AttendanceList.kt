package com.segen.kd.fragment.home.attendance

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.segen.kd.R
import com.segen.kd.databinding.FragmentAttendanceListListBinding
import com.segen.kd.modelbody.AttModel
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.utility.GentUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * A fragment representing a list of Items.
 */
class AttendanceList : Fragment() {

    private lateinit var binding: FragmentAttendanceListListBinding
    private var inputData: MutableList<AttModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAttendanceListListBinding.inflate(inflater, container, false)
        background()
        return binding.root
    }

    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@AttendanceList.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )
            val obj = IdOnly(
                sharedPref.getString(
                    getString(R.string.loginIdPref),
                    ""
                )
            )

            onSuccess(
                GentUtil().process(
                    this@AttendanceList.resources.getString(R.string.attendanceList),
                    obj,
                    this@AttendanceList.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        val array: JSONArray = resp.getJSONArray("data")
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            inputData.add(Gson().fromJson(item.toString(), AttModel::class.java))
        }
        withContext(Dispatchers.Main) {
            if (view is RecyclerView) {
                val myItemRecyclerViewAdapter =
                    MyItemRecyclerViewAdapter(inputData, requireContext())
                binding.list.adapter = myItemRecyclerViewAdapter

            }
        }
    }

}