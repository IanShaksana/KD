package com.example.kd.fragment.manager.submission.deposito.core

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
import com.example.kd.databinding.FragmentDepositoManageListBinding
import com.example.kd.modelbody.IdOnly
import com.example.kd.modelbody.TaskModelLoan
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
class DepositoManage : Fragment() {


    private lateinit var binding: FragmentDepositoManageListBinding
    private lateinit var inputData: MutableList<TaskModelLoan>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepositoManageListBinding.inflate(inflater,container,false)
        background()
        return binding.root
    }


    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@DepositoManage.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )
            onSuccess(
                getDepo(
                    this@DepositoManage.requireContext().resources.getString(R.string.submitDepositoGetManager),
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

    private fun getDepo(url: String, jObject: JSONObject): MutableList<TaskModelLoan> {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this.requireContext())
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        val data: MutableList<TaskModelLoan> = ArrayList()
        try {
            val resp = future.get(15, TimeUnit.SECONDS)
            //val resp = future.get()
            val array: JSONArray = resp.getJSONArray("data")
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                data.add(Gson().fromJson(item.toString(), TaskModelLoan::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data

    }

    private suspend fun onSuccess(resp: MutableList<TaskModelLoan>) {
        withContext(Dispatchers.Main) {
            inputData = resp

            val myItemRecyclerViewAdapter =
                MyItemRecyclerViewAdapter2(inputData, requireContext())
            binding.list.adapter = myItemRecyclerViewAdapter


            myItemRecyclerViewAdapter.onItemClick = {
                val action =
                    DepositoManageDirections.actionNavDepositoManagerToDepositoManageDetail(it.id)
                binding.root.findNavController().navigate(action)

            }

        }
    }


}