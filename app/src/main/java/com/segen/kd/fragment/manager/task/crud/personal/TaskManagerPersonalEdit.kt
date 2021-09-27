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
import androidx.navigation.fragment.navArgs
import com.segen.kd.R
import com.segen.kd.databinding.FragmentTaskManagerPersonalEditBinding
import com.segen.kd.dialog.DialogDate
import com.segen.kd.dialog.marketing.DialogMarketing
import com.segen.kd.dialog.marketing.DialogMarketing2
import com.segen.kd.dialog.marketing.MarketingAdapter
import com.segen.kd.modelbody.EditPersonalModel
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.modelbody.ListMarketingModel
import com.segen.kd.utility.GentUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TaskManagerPersonalEdit : Fragment(), DatePickerDialog.OnDateSetListener,
    DialogMarketing.dialogListenerMarketing, DialogMarketing2.dialogListenerMarketing2 {

    private lateinit var binding: FragmentTaskManagerPersonalEditBinding
    private val value: TaskManagerPersonalEditArgs by navArgs()
    private lateinit var data: JSONObject
    private var chosenMarketing = ""
    private lateinit var adapterMarketing: ArrayAdapter<ListMarketingModel>
    private lateinit var adapterMarketing2: MarketingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskManagerPersonalEditBinding.inflate(inflater, container, false)
        backgroundInitial1()
        binding.marketing.setOnClickListener {
            val dialog = DialogMarketing2(adapterMarketing2)
            dialog.show(childFragmentManager, "Dialog Marketing")
        }
        binding.deadline.setOnClickListener {
            val dialog = DialogDate()
            dialog.show(childFragmentManager, "")
        }
        binding.edit.setOnClickListener {
            var fields: Array<TextInputEditText> = arrayOf(
                binding.pengajuanTitle,
                binding.marketing,
                binding.deadline,
                binding.description,
            )
            if (validate(fields)) {
                backgroundEdit()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ada Form Yang Belum Terisi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    private fun backgroundInitial1() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref =
                this@TaskManagerPersonalEdit.requireActivity().getSharedPreferences(
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
                    this@TaskManagerPersonalEdit.resources.getString(R.string.getMarketing),
                    obj,
                    this@TaskManagerPersonalEdit.requireContext()
                )
            )

        }
    }

    private suspend fun onSuccess1(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            val input: MutableList<ListMarketingModel> = ArrayList()
            val array: JSONArray = resp.getJSONArray("data")
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                input.add(Gson().fromJson(item.toString(), ListMarketingModel::class.java))
            }
            adapterMarketing = ArrayAdapter<ListMarketingModel>(
                requireContext(),
                android.R.layout.select_dialog_singlechoice
            )

            adapterMarketing2 = MarketingAdapter(requireContext(), input)
            adapterMarketing.addAll(input)

            backgroundInitial()
        }
    }

    private fun backgroundInitial() {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdOnly(
                value.idtask
            )
            onSuccess(
                GentUtil().process(
                    this@TaskManagerPersonalEdit.resources.getString(R.string.getTaskDetailManager),
                    obj,
                    this@TaskManagerPersonalEdit.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            val data = resp.getJSONArray("data").getJSONObject(0)
            this@TaskManagerPersonalEdit.data = data
            binding.apply {
                pengajuanTitle.setText(data.getString("title"))

                chosenMarketing = data.getString("idmarketing")
                for (i in 0..adapterMarketing.count - 1) {
                    val item: ListMarketingModel = adapterMarketing.getItem(i)!!
                    if (item.id.equals(chosenMarketing, ignoreCase = true)) {
                        marketing.setText(item.nama)
                        break
                    }
                }


                val deadlineText = DateTime(data.getString("detailDeadline"))
                val valueDeadline =
                    deadlineText.toString(activity?.resources?.getString(R.string.format_date_1))
                deadline.setText(valueDeadline)
                description.setText("${data.getString("detailDesc")}")
                

            }

        }


    }

    private fun backgroundEdit() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@TaskManagerPersonalEdit.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )

            val obj = EditPersonalModel(
                value.idtask,
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
                binding.description.text.toString(),
                data.getInt("version")
            )

            onSuccessEdit(
                GentUtil().process(
                    this@TaskManagerPersonalEdit.resources.getString(R.string.editTaskManager),
                    obj,
                    this@TaskManagerPersonalEdit.requireContext()
                )
            )

        }
    }

    private suspend fun onSuccessEdit(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                Toast.makeText(
                    requireContext(),
                    "Tugas Berhasil Diubah",
                    Toast.LENGTH_SHORT
                ).show()
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

    override fun onDialogClickMarketing(value1: String, value2: String) {
        chosenMarketing = value1
        binding.marketing.setText(value2)
    }

    override fun onDialogClickMarketing2(value1: String, value2: String) {
        chosenMarketing = value1
        binding.marketing.setText(value2)
    }

}