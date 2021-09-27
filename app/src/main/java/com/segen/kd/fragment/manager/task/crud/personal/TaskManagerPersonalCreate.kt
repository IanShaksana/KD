package com.segen.kd.fragment.manager.task.crud.personal

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.navigation.findNavController
import com.segen.kd.R
import com.segen.kd.databinding.FragmentTaskManagerPersonalCreateBinding
import com.segen.kd.dialog.DialogDate
import com.segen.kd.dialog.marketing.DialogMarketing
import com.segen.kd.dialog.marketing.DialogMarketing2
import com.segen.kd.dialog.marketing.MarketingAdapter
import com.segen.kd.modelbody.CreatePersonalModel
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.modelbody.ListMarketingModel
import com.segen.kd.utility.GentUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TaskManagerPersonalCreate : Fragment(), DialogMarketing.dialogListenerMarketing,
    DatePickerDialog.OnDateSetListener, DialogMarketing2.dialogListenerMarketing2 {

    private lateinit var binding: FragmentTaskManagerPersonalCreateBinding
    private lateinit var adapterMarketing: ArrayAdapter<ListMarketingModel>
    private lateinit var adapterMarketing2: MarketingAdapter

    private var chosenMarketing = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskManagerPersonalCreateBinding.inflate(inflater, container, false)
        backgroundInitial1()
        binding.marketing.setOnClickListener {
            val dialog = DialogMarketing2(adapterMarketing2)
            dialog.show(childFragmentManager, "Dialog Marketing")
        }
        binding.deadline.setOnClickListener {
            val dialog = DialogDate()
            dialog.show(childFragmentManager, "")
        }
        binding.create.setOnClickListener {
            var fields: Array<TextInputEditText> = arrayOf(
                binding.pengajuanTitle,
                binding.marketing,
                binding.deadline,
                binding.description,
            )
            if (validate(fields)) {
                backgroundCreate()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ada Bagian Yang Belum Terisi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    private fun backgroundInitial1() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref =
                this@TaskManagerPersonalCreate.requireActivity().getSharedPreferences(
                    getString(R.string.credPref), Context.MODE_PRIVATE
                )
            val obj = IdOnly(
                sharedPref.getString(
                    getString(R.string.loginIdPref),
                    ""
                )
            )

            onSuccess1(
                GentUtil().process(
                    this@TaskManagerPersonalCreate.resources.getString(R.string.getMarketing),
                    obj,
                    this@TaskManagerPersonalCreate.requireContext()
                )
            )


        }
    }

    private suspend fun onSuccess1(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            val data: MutableList<ListMarketingModel> = ArrayList()
            val array: JSONArray = resp.getJSONArray("data")
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                data.add(Gson().fromJson(item.toString(), ListMarketingModel::class.java))
            }

            adapterMarketing = ArrayAdapter<ListMarketingModel>(
                requireContext(),
                android.R.layout.select_dialog_singlechoice
            )
            adapterMarketing2 = MarketingAdapter(requireContext(), data)
            adapterMarketing.addAll(data)
        }
    }

    private fun backgroundCreate() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref =
                this@TaskManagerPersonalCreate.requireActivity().getSharedPreferences(
                    getString(R.string.credPref), Context.MODE_PRIVATE
                )

            val obj = CreatePersonalModel(
                chosenMarketing,
                binding.pengajuanTitle.text.toString(),
                "Personal",
                binding.deadline.text.toString(),
                binding.description.text.toString(),
                sharedPref.getString(
                    requireContext().getString(R.string.loginIdPref),
                    ""
                )!!,
                sharedPref.getString(
                    requireContext().getString(R.string.loginIdPref),
                    ""
                )!!,
                binding.description.text.toString()
            )

            onSuccess(
                GentUtil().process(
                    this@TaskManagerPersonalCreate.resources.getString(R.string.createTaskManager),
                    obj,
                    this@TaskManagerPersonalCreate.requireContext()
                )
            )

        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                Toast.makeText(
                    requireContext(),
                    "Tugas Berhasil Dibuat",
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.findNavController().popBackStack()
                binding.root.findNavController().popBackStack()
            }
        }
    }

    private fun validate(fields: Array<TextInputEditText>): Boolean {
        for (i in fields.indices) {
            val currentField = fields[i]
            if (currentField.text.toString().isEmpty()) {
                return false
            }
        }
        return true
    }

    override fun onDialogClickMarketing(value1: String, value2: String) {
        chosenMarketing = value1
        binding.marketing.setText(value2)
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val c = Calendar.getInstance()
        c[Calendar.YEAR] = p1
        c[Calendar.MONTH] = p2
        c[Calendar.DAY_OF_MONTH] = p3
        val format1 = SimpleDateFormat(requireContext().resources.getString(R.string.format_date_1))
        val currentDate = format1.format(c.time)
        val dt = LocalDate(c.time)
        val today = LocalDate(Date())
        if (!dt.isBefore(today)) {
            binding.deadline.setText(currentDate)
        } else {
            Toast.makeText(
                requireContext(),
                "Tanggal Tidak Boleh Kurang Dari Tanggal Hari Ini",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDialogClickMarketing2(value1: String, value2: String) {
        chosenMarketing = value1
        binding.marketing.setText(value2)
    }


}