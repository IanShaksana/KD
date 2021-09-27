package com.segen.kd.fragment.marketing.submission.loan.core

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.segen.kd.R
import com.segen.kd.databinding.FragmentSub31LoanListBinding
import com.segen.kd.dialog.DialogStatusSub
import com.segen.kd.modelbody.IdMessageOnly
import com.segen.kd.modelbody.TaskModelLoan
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
class Sub31Loan : Fragment(), DialogStatusSub.dialogListener {


    private lateinit var binding: FragmentSub31LoanListBinding
    private lateinit var inputData: MutableList<TaskModelLoan>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSub31LoanListBinding.inflate(inflater, container, false)
        background("ALL")
        binding.fab.setOnClickListener {
            val action =
                Sub31LoanDirections.actionNavLoanSubmitToSub31LoanCreate()
            binding.root.findNavController().navigate(action)
        }

        binding.statusCard.setOnClickListener {
            val dialog = DialogStatusSub()
            dialog.show(childFragmentManager, "Dialog Loan Produk")
        }

        return binding.root
    }


    private fun background(status: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@Sub31Loan.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )
            val obj = IdMessageOnly(
                sharedPref.getString(
                    getString(R.string.loginIdPref),
                    ""
                ),
                status
            )

            onSuccess(
                GentUtil().process(
                    this@Sub31Loan.resources.getString(R.string.submitLoanGet),
                    obj,
                    this@Sub31Loan.requireContext()
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
                inputData.add(Gson().fromJson(item.toString(), TaskModelLoan::class.java))
            }

            val myItemRecyclerViewAdapter =
                MyItemRecyclerViewAdapter(inputData, requireContext())
            binding.list.adapter = myItemRecyclerViewAdapter

            myItemRecyclerViewAdapter.onItemClick = {
                val action =
                    Sub31LoanDirections.actionNavLoanSubmitToSub01LoanDetail(it.id)
                binding.root.findNavController().navigate(action)

            }

        }
    }

    override fun onDialogStatusSubClick(value: String) {
        binding.statusText.text = value
        background(value)
    }


}