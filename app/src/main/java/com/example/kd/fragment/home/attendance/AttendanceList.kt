package com.example.kd.fragment.home.attendance

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.FragmentAttendanceListListBinding
import com.example.kd.modelbody.AttModel
import com.example.kd.modelbody.IdOnly
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * A fragment representing a list of Items.
 */
class AttendanceList : Fragment() {


    private lateinit var binding: FragmentAttendanceListListBinding
    private lateinit var inputData: MutableList<AttModel>

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
            onSuccess(
                getTask(
                    this@AttendanceList.requireContext().resources.getString(R.string.attendanceList),
                    JSONObject(
                        Gson().toJson(
                            IdOnly(
                                sharedPref.getString(
                                    getString(R.string.loginIdPref),
                                    ""
                                )
                            )
                        )
                    )
                )
            )
        }
    }

    private fun getTask(url: String, jObject: JSONObject): MutableList<AttModel> {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this.requireContext())
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        val data: MutableList<AttModel> = ArrayList()
        try {
            val resp = future.get(15, TimeUnit.SECONDS)
            //val resp = future.get()
            val array: JSONArray = resp.getJSONArray("data")
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                data.add(Gson().fromJson(item.toString(), AttModel::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data

    }

    private suspend fun onSuccess(resp: MutableList<AttModel>) {
        withContext(Dispatchers.Main) {
            inputData = resp

            if (view is RecyclerView) {
                val myItemRecyclerViewAdapter = MyItemRecyclerViewAdapter(inputData,requireContext())
                binding.list.adapter = myItemRecyclerViewAdapter

            }
        }
    }


}