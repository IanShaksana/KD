package com.segen.kd.fragment.manager.submission.loan.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.segen.kd.R
import com.segen.kd.databinding.FragmentLoanManageListBinding
import com.segen.kd.dialog.DialogStatusSub
import com.segen.kd.fragment.marketing.submission.loan.core.MyItemRecyclerViewAdapter
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.modelbody.TaskModelLoan
import com.segen.kd.utility.GentUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class LoanManage : Fragment(), DialogStatusSub.dialogListener {

    private lateinit var binding: FragmentLoanManageListBinding
    private lateinit var inputData: MutableList<TaskModelLoan>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoanManageListBinding.inflate(inflater,container,false)
        background("ALL")

        binding.statusCard.setOnClickListener {
            val dialog = DialogStatusSub()
            dialog.show(childFragmentManager, "Dialog Loan Produk")
        }
        return binding.root
    }

    private fun background(status:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdOnly(
                status
            )

            onSuccess(
                GentUtil().process(
                    this@LoanManage.resources.getString(R.string.submitLoanGetManager),
                    obj,
                    this@LoanManage.requireContext()
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
                    LoanManageDirections.actionNavLoanManagerToLoanManageDetail(it.id)
                binding.root.findNavController().navigate(action)

            }

        }
    }

    override fun onDialogStatusSubClick(value: String) {
        binding.statusText.text = value
        background(value)
    }

}