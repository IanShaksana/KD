package com.example.kd.fragment.marketing.submission.deposito.core

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
import com.example.kd.databinding.FragmentSub32DepositoListBinding
import com.example.kd.dialog.DialogStatusSub
import com.example.kd.dialog.DialogStatusTugas
import com.example.kd.modelbody.IdMessageOnly
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
class Sub32Deposito : Fragment(), DialogStatusSub.dialogListener {


    private lateinit var binding: FragmentSub32DepositoListBinding
    private lateinit var inputData: MutableList<TaskModelLoan>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSub32DepositoListBinding.inflate(inflater, container, false)
        background("ALL")

        binding.fab.setOnClickListener {
            val action =
                Sub32DepositoDirections.actionNavDepositoSubmitToSub32DepositoCreate()
            binding.root.findNavController().navigate(action)
        }

        binding.statusCard.setOnClickListener {
            val dialog = DialogStatusSub()
            dialog.show(childFragmentManager, "Dialog Loan Produk")
        }

        return binding.root
    }


    private fun background(status:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@Sub32Deposito.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )
            onSuccess(
                getDepo(
                    this@Sub32Deposito.requireContext().resources.getString(R.string.submitDepositoGet),
                    JSONObject(
                        Gson().toJson(
                            IdMessageOnly(
                                sharedPref.getString(
                                    getString(R.string.loginIdPref),
                                    ""
                                ),
                                status
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
                MyItemRecyclerViewAdapter(inputData, requireContext())
            binding.list.adapter = myItemRecyclerViewAdapter


            myItemRecyclerViewAdapter.onItemClick = {
                val action =
                    Sub32DepositoDirections.actionNavDepositoSubmitToSub02DepositoDetail(it.id)
                binding.root.findNavController().navigate(action)

            }

        }
    }

    override fun onDialogStatusSubClick(value: String) {
        binding.statusText.text = value
        background(value)
    }


}