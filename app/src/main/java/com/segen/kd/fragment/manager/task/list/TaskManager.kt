package com.segen.kd.fragment.manager.task.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.segen.kd.R
import com.segen.kd.databinding.FragmentTaskManagerListBinding
import com.segen.kd.dialog.DialogStatusTugas
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.modelbody.TaskModelManager
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
class TaskManager : Fragment(), DialogStatusTugas.dialogListener {

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
        binding.statusCard.setOnClickListener {
            val dialog = DialogStatusTugas()
            dialog.show(childFragmentManager, "Dialog Loan Produk")
        }
        background("ALL")
        return binding.root
    }

    private fun background(status:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdOnly(
                status
            )

            onSuccess(
                GentUtil().process(
                    this@TaskManager.resources.getString(R.string.getTaskManager),
                    obj,
                    this@TaskManager.requireContext()
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
                inputData.add(Gson().fromJson(item.toString(), TaskModelManager::class.java))
            }

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

    override fun onDialogStatusTugasClick(value: String) {
        binding.statusText.text = value
        background(value)
    }

}