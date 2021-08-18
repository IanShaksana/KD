package com.example.kd.fragment.marketing.task.core

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.Frag21Collection01Binding
import com.example.kd.modelbody.IdOnly
import com.example.kd.modelbody.TaskModel
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
class Task01Collection : Fragment() {

    private lateinit var binding: Frag21Collection01Binding
    private lateinit var inputData: MutableList<TaskModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Frag21Collection01Binding.inflate(inflater, container, false)
        background()
        return binding.root
    }

    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@Task01Collection.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )
            onSuccess(
                getTask(
                    this@Task01Collection.requireContext().resources.getString(R.string.getTask),
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

    private fun getTask(url: String, jObject: JSONObject): MutableList<TaskModel> {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this.requireContext())
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        val data: MutableList<TaskModel> = ArrayList()
        try {
            val resp = future.get(15, TimeUnit.SECONDS)
            //val resp = future.get()
            val array: JSONArray = resp.getJSONArray("data")
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                data.add(Gson().fromJson(item.toString(), TaskModel::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data

    }

    private suspend fun onSuccess(resp: MutableList<TaskModel>) {
        withContext(Dispatchers.Main) {
            inputData = resp

            if (view is RecyclerView) {

                val myItemRecyclerViewAdapter =
                    MyItemRecyclerViewAdapter(inputData, requireContext())
                binding.list.adapter = myItemRecyclerViewAdapter


                myItemRecyclerViewAdapter.onItemClick = {
                    val check: String = it.tipe
                    if (check.equals("Personal", true)) {
                        val action =
                            Task01CollectionDirections.actionNavCollectionToTask02TaskDetail(it.id)
                        binding.root.findNavController().navigate(action)
                    } else {
                        val action =
                            Task01CollectionDirections.actionNavCollectionToTask01CollectionDetail(
                                it.id
                            )
                        binding.root.findNavController().navigate(action)
                    }
                }
            }
        }
    }

}