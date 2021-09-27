package com.segen.kd.fragment.marketing.task.core

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.segen.kd.R
import com.segen.kd.databinding.Frag21Collection01Binding
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.modelbody.TaskModel
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

            val obj = IdOnly(
                sharedPref.getString(
                    getString(R.string.loginIdPref),
                    ""
                )
            )

            onSuccess(
                GentUtil().process(
                    this@Task01Collection.resources.getString(R.string.getTask),
                    obj,
                    this@Task01Collection.requireContext()
                )
            )

        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            inputData = ArrayList()
            val array: JSONArray = resp.getJSONArray("data")
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                inputData.add(Gson().fromJson(item.toString(), TaskModel::class.java))
            }

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