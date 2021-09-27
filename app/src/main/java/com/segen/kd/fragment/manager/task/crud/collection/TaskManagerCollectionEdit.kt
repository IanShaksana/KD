package com.segen.kd.fragment.manager.task.crud.collection

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.segen.kd.R
import com.segen.kd.databinding.FragmentTaskManagerCollectionEditBinding
import com.segen.kd.dialog.DialogDate
import com.segen.kd.dialog.collection.CollectionAdapter
import com.segen.kd.dialog.collection.DialogCollection2
import com.segen.kd.dialog.marketing.DialogMarketing2
import com.segen.kd.dialog.marketing.MarketingAdapter
import com.segen.kd.modelbody.*
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
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskManagerCollectionEdit : Fragment(), DatePickerDialog.OnDateSetListener,
    DialogCollection2.dialogListenerCollection, DialogMarketing2.dialogListenerMarketing2 {

    private lateinit var binding: FragmentTaskManagerCollectionEditBinding
    private val value: TaskManagerCollectionEditArgs by navArgs()
    private lateinit var adapterMarketing: ArrayAdapter<ListMarketingModel>
    private lateinit var adapterMarketing2: MarketingAdapter
    private lateinit var adapterCollection: ArrayAdapter<ListCollectionModel>
    private lateinit var adapterCollection2: CollectionAdapter
    private lateinit var data: JSONObject

    private var choosenMarketing = ""
    private var choosenCollection = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTaskManagerCollectionEditBinding.inflate(inflater, container, false)
        backgroundInitial1()

        binding.marketing.setOnClickListener {
            val dialog = DialogMarketing2(adapterMarketing2)
            dialog.show(childFragmentManager, "Dialog Marketing")
        }
        binding.collection.setOnClickListener {
            val dialog = DialogCollection2(adapterCollection2)
            dialog.show(childFragmentManager, "Dialog Marketing")
        }
        binding.deadline.setOnClickListener {
            val dialog = DialogDate()
            dialog.show(childFragmentManager, "")
        }
        binding.edit.setOnClickListener {
            val fields: Array<TextInputEditText> = arrayOf(
                binding.deadline,
                binding.marketing,
                binding.collection
            )
            if (validate(fields)) {
                backgroundCreate()
            }else{
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
                this@TaskManagerCollectionEdit.requireActivity().getSharedPreferences(
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
                    this@TaskManagerCollectionEdit.resources.getString(R.string.getMarketing),
                    obj,
                    this@TaskManagerCollectionEdit.requireContext()
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
            adapterMarketing.addAll(input)
            adapterMarketing2 = MarketingAdapter(requireContext(), input)

            backgroundInitial2()
        }
    }

    private fun backgroundInitial2() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref =
                this@TaskManagerCollectionEdit.requireActivity().getSharedPreferences(
                    getString(R.string.credPref), Context.MODE_PRIVATE
                )
            val obj = IdOnly(
                sharedPref.getString(
                    getString(R.string.loginIdPref),
                    ""
                )
            )

            onSuccess2(
                GentUtil().process(
                    this@TaskManagerCollectionEdit.resources.getString(R.string.getCollection),
                    obj,
                    this@TaskManagerCollectionEdit.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccess2(resp: JSONObject) {
        val input: MutableList<ListCollectionModel> = ArrayList()
        withContext(Dispatchers.Main) {
            val array: JSONArray = resp.getJSONArray("data")
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                input.add(Gson().fromJson(item.toString(), ListCollectionModel::class.java))
            }
            adapterCollection2 = CollectionAdapter(requireContext(), input)
            adapterCollection = ArrayAdapter<ListCollectionModel>(
                requireContext(),
                android.R.layout.select_dialog_singlechoice
            )
            adapterCollection.addAll(input)
            backgroundInitial3()
        }
    }

    private fun backgroundInitial3() {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdOnly(
                value.idtask
            )

            onSuccess(
                GentUtil().process(
                    this@TaskManagerCollectionEdit.resources.getString(R.string.getTaskDetailManager),
                    obj,
                    this@TaskManagerCollectionEdit.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            val data = resp.getJSONArray("data").getJSONObject(0)
            Timber.i(data.toString())
            this@TaskManagerCollectionEdit.data = data
            binding.apply {
                // pengajuanTitle.setText(data.getString("title"))

                val deadlineText = DateTime(data.getString("detailDeadline"))
                val valueDeadline =
                    deadlineText.toString(activity?.resources?.getString(R.string.format_date_1))
                deadline.setText(valueDeadline)
                description.setText(data.getString("detailDesc"))

                choosenMarketing = data.getString("idmarketing")
                for (i in 0 until adapterMarketing.count - 1) {
                    val item: ListMarketingModel = adapterMarketing.getItem(i)!!
                    if (item.id.equals(choosenMarketing, ignoreCase = true)) {
                        marketing.setText(item.nama)
                        break
                    }
                }

                choosenCollection = data.getString("idcollection")
                for (i in 0 until adapterCollection.count - 1) {
                    val item: ListCollectionModel = adapterCollection.getItem(i)!!
                    if (item.id.equals(choosenCollection, ignoreCase = true)) {
                        collection.setText(item.nama + "-" + item.nopk)
                        binding.apply {
                            anggotaNama.text = "Nama : ${item.nama}"
                            anggotaAlamat.text = "Alamat : ${item.alamat}"
                            anggotaKontak.text = "Kontak : ${item.kontak}"

                            val tanggal = DateTime(item.angsuranTanggal)
                            val valueTanggal =
                                tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                            angsuranTanggal.text = "Tanggal : $valueTanggal"
                            angsuranNominal.text = "Nominal : ${item.angsuranNominal}"
                            angsuranDenda.text = "Denda : ${item.angsuranDenda}"


                            noPK.text = "No PK : ${item.nopk}"
                            fasilitasDenda.text = "Total denda : ${item.nominaldenda}"
                            fasilitasStatusDenda.text = "Status denda : ${item.flagdenda}"
                        }
                        break
                    }
                }


//                val deadlineText = DateTime(data.getString("detailDeadline"))
//                val valueDeadline =
//                    deadlineText.toString(activity?.resources?.getString(R.string.format_date_1))
//                deadline.setText(valueDeadline)
//                description.setText("${data.getString("detailDesc")}")

                var fields: Array<TextView> = arrayOf(
                    binding.anggotaNama,
                    binding.anggotaAlamat,
                    binding.anggotaKontak,
                    binding.angsuranTanggal,
                    binding.angsuranNominal,
                    binding.angsuranDenda,
                )
                validate(fields)
            }

        }


    }

    private fun backgroundCreate() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref =
                this@TaskManagerCollectionEdit.requireActivity().getSharedPreferences(
                    getString(R.string.credPref), Context.MODE_PRIVATE
                )
            val obj = EditCollectionModel(
                value.idtask,
                choosenMarketing,
                choosenCollection,
                binding.deadline.text.toString(),
                binding.description.text.toString(),
                sharedPref.getString(
                    requireContext().getString(R.string.loginIdPref),
                    ""
                )!!,
                data.getInt("version")
            )

            onSuccessCreate(
                GentUtil().process(
                    this@TaskManagerCollectionEdit.resources.getString(R.string.editTaskManager),
                    obj,
                    this@TaskManagerCollectionEdit.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccessCreate(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                Toast.makeText(
                    requireContext(),
                    "Tugas Berhasil Diubah",
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.findNavController().popBackStack()
            }else{
                Toast.makeText(
                    requireContext(),
                    resp.getString("message"),
                    Toast.LENGTH_SHORT
                ).show()
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

    override fun onDialogClickCollection(value1: String, value2: Int) {
        val dataPreview = adapterCollection.getItem(value2)!!
        this.choosenCollection = value1
        binding.apply {
            collection.setText(dataPreview.nama + "-" + dataPreview.nopk)
            anggotaNama.text = "Nama : ${dataPreview.nama}"
            anggotaAlamat.text = "Alamat : ${dataPreview.alamat}"
            anggotaKontak.text = "Kontak : ${dataPreview.kontak}"

            val tanggal = DateTime(dataPreview.angsuranTanggal)
            val valueTanggal =
                tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
            angsuranTanggal.text = "Tanggal : $valueTanggal"
            angsuranNominal.text = "Nominal : ${dataPreview.angsuranNominal}"
            angsuranDenda.text = "Denda : ${dataPreview.angsuranDenda}"


            noPK.text = "No PK : ${dataPreview.nopk}"
            fasilitasDenda.text = "Total denda : ${dataPreview.nominaldenda}"
            fasilitasStatusDenda.text = "Status denda : ${dataPreview.flagdenda}"

            var fields: Array<TextView> = arrayOf(
                binding.anggotaNama,
                binding.anggotaAlamat,
                binding.anggotaKontak,
                binding.angsuranTanggal,
                binding.angsuranNominal,
                binding.angsuranDenda,
            )
            validate(fields)
        }
    }

    override fun onDialogClickMarketing2(value1: String, value2: String) {
        choosenMarketing = value1
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

    private fun validate(fields: Array<TextView>): Boolean {
        for (i in fields.indices) {
            val currentField = fields[i]

            if (currentField.text.toString().contains("null")) {
                val value = currentField.text.toString().split("null").get(0)
                currentField.text = value +"-"
            }
        }
        return true
    }

}