package com.example.kd.fragment.manager.task.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.FragmentTaskManagerListBinding
import com.example.kd.modelbody.IdOnly
import com.example.kd.modelbody.TaskModelManager
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
class TaskManager : Fragment() {

    private lateinit var binding: FragmentTaskManagerListBinding
    private lateinit var inputData: MutableList<TaskModelManager>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskManagerListBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener {
            val action = TaskManagerDirections.actionTaskManagerToTaskManagerCrudSelect()
            binding.root.findNavController().navigate(action)
        }
        background()
        return binding.root
    }


    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@TaskManager.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )
            onSuccess(
                getTask(
                    this@TaskManager.requireContext().resources.getString(R.string.getTaskManager),
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

    private fun getTask(url: String, jObject: JSONObject): MutableList<TaskModelManager> {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this.requireContext())
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        val data: MutableList<TaskModelManager> = ArrayList()
        try {
            val resp = future.get(15, TimeUnit.SECONDS)
            //val resp = future.get()
            val array: JSONArray = resp.getJSONArray("data")
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                data.add(Gson().fromJson(item.toString(), TaskModelManager::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data

    }

    private suspend fun onSuccess(resp: MutableList<TaskModelManager>) {
        withContext(Dispatchers.Main) {
            inputData = resp

            val myItemRecyclerViewAdapter =
                MyItemRecyclerViewAdapter(inputData, requireContext())
            binding.list.adapter = myItemRecyclerViewAdapter


            myItemRecyclerViewAdapter.onItemClick = {
                val check: String = it.tipe
                if (check.equals("Personal", true)) {
                    val action =
                        TaskManagerDirections.actionTaskManagerToTaskManagerPersonalDetail(it.id)
                    binding.root.findNavController().navigate(action)
                } else {
                    val action =
                        TaskManagerDirections.actionTaskManagerToTaskManagerCollectionDetail(
                            it.id
                        )
                    binding.root.findNavController().navigate(action)
                }
            }

        }
    }


}