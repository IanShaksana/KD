package com.example.kd.fragment.manager.task.crud.personal

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
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.FragmentTaskManagerPersonalCreateBinding
import com.example.kd.dialog.DialogDate
import com.example.kd.dialog.marketing.DialogMarketing
import com.example.kd.dialog.marketing.DialogMarketing2
import com.example.kd.dialog.marketing.MarketingAdapter
import com.example.kd.modelbody.CreatePersonalModel
import com.example.kd.modelbody.IdOnly
import com.example.kd.modelbody.ListMarketingModel
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
import java.util.concurrent.TimeUnit
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
//            val dialog = DialogMarketing(adapterMarketing)
//            dialog.show(childFragmentManager, "Dialog Marketing")

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
            onSuccess1(
                getMarketing(
                    this@TaskManagerPersonalCreate.requireContext().resources.getString(R.string.getMarketing),
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

    private fun getMarketing(url: String, jObject: JSONObject): MutableList<ListMarketingModel> {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this.requireContext())
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        val data: MutableList<ListMarketingModel> = ArrayList()
        try {
            val resp = future.get(15, TimeUnit.SECONDS)
            //val resp = future.get()
            val array: JSONArray = resp.getJSONArray("data")
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                data.add(Gson().fromJson(item.toString(), ListMarketingModel::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data

    }

    private suspend fun onSuccess1(resp: MutableList<ListMarketingModel>) {
        withContext(Dispatchers.Main) {
            adapterMarketing = ArrayAdapter<ListMarketingModel>(
                requireContext(),
                android.R.layout.select_dialog_singlechoice
            )
            adapterMarketing2 = MarketingAdapter(requireContext(), resp)
            adapterMarketing.addAll(resp)
        }
    }

    private fun backgroundCreate() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref =
                this@TaskManagerPersonalCreate.requireActivity().getSharedPreferences(
                    getString(R.string.credPref), Context.MODE_PRIVATE
                )

            onSuccess(
                create(
                    this@TaskManagerPersonalCreate.requireContext().resources.getString(R.string.createTaskManager),
                    JSONObject(
                        Gson().toJson(
                            CreatePersonalModel(
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
                                binding.deadline.text.toString(),
                                binding.description.text.toString()
                            )
                        )
                    )
                )
            )
        }
    }

    private fun create(url: String, jObject: JSONObject): JSONObject {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this.requireContext())
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        var resp = JSONObject()
        try {
            resp = future.get(15, TimeUnit.SECONDS)
            //val resp = future.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resp

    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
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