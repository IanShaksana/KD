package com.example.kd.fragment.manager.task.crud.collection

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.FragmentTaskManagerCollectionCreateBinding
import com.example.kd.dialog.collection.CollectionAdapter
import com.example.kd.dialog.collection.DialogCollection
import com.example.kd.dialog.collection.DialogCollection2
import com.example.kd.dialog.marketing.DialogMarketing
import com.example.kd.dialog.marketing.DialogMarketing2
import com.example.kd.dialog.marketing.MarketingAdapter
import com.example.kd.modelbody.*
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class TaskManagerCollectionCreate : Fragment(),
    DialogCollection2.dialogListenerCollection, DialogMarketing2.dialogListenerMarketing2 {

    private lateinit var binding: FragmentTaskManagerCollectionCreateBinding
    private lateinit var adapterMarketing: ArrayAdapter<ListMarketingModel>
    private lateinit var adapterMarketing2: MarketingAdapter
    private lateinit var adapterCollection: ArrayAdapter<ListCollectionModel>
    private lateinit var adapterCollection2: CollectionAdapter

    private var choosenMarketing = ""
    private var choosenCollection = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskManagerCollectionCreateBinding.inflate(inflater, container, false)
        backgroundInitial1()
        backgroundInitial2()
        binding.marketing.setOnClickListener {
            val dialog = DialogMarketing2(adapterMarketing2)
            dialog.show(childFragmentManager, "Dialog Marketing")
        }
        binding.collection.setOnClickListener {
            val dialog = DialogCollection2(adapterCollection2)
            dialog.show(childFragmentManager, "Dialog Marketing")
        }
        binding.create.setOnClickListener {
            var fields: Array<TextInputEditText> = arrayOf(
                binding.pengajuanTitle,
                binding.marketing,
                binding.collection
            )
            if (validate(fields)) {
                backgroundCreate()
            }

        }
        return binding.root
    }

    private fun backgroundInitial1() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref =
                this@TaskManagerCollectionCreate.requireActivity().getSharedPreferences(
                    getString(R.string.credPref), Context.MODE_PRIVATE
                )
            onSuccess1(
                getMarketing(
                    this@TaskManagerCollectionCreate.requireContext().resources.getString(R.string.getMarketing),
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

    private fun backgroundInitial2() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref =
                this@TaskManagerCollectionCreate.requireActivity().getSharedPreferences(
                    getString(R.string.credPref), Context.MODE_PRIVATE
                )
            onSuccess2(
                getCollection(
                    this@TaskManagerCollectionCreate.requireContext().resources.getString(R.string.getCollection),
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

    private fun getCollection(url: String, jObject: JSONObject): MutableList<ListCollectionModel> {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this.requireContext())
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        val data: MutableList<ListCollectionModel> = ArrayList()
        try {
            val resp = future.get(15, TimeUnit.SECONDS)
            //val resp = future.get()
            val array: JSONArray = resp.getJSONArray("data")
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                data.add(Gson().fromJson(item.toString(), ListCollectionModel::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data

    }

    private suspend fun onSuccess2(resp: MutableList<ListCollectionModel>) {
        withContext(Dispatchers.Main) {
            adapterCollection = ArrayAdapter<ListCollectionModel>(
                requireContext(),
                android.R.layout.select_dialog_singlechoice
            )
            adapterCollection2 = CollectionAdapter(requireContext(), resp)
            adapterCollection.addAll(resp)
        }
    }

    private fun backgroundCreate() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref =
                this@TaskManagerCollectionCreate.requireActivity().getSharedPreferences(
                    getString(R.string.credPref), Context.MODE_PRIVATE
                )
            val tanggal = DateTime(Date())
            val valueTanggal =
                tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
            onSuccess(
                create(
                    this@TaskManagerCollectionCreate.requireContext().resources.getString(R.string.createTaskManager),
                    JSONObject(
                        Gson().toJson(
                            CreateCollectionModel(
                                choosenMarketing,
                                choosenCollection,
                                binding.pengajuanTitle.text.toString(),
                                "COLLECTION",
                                valueTanggal,
                                "",
                                sharedPref.getString(
                                    requireContext().getString(R.string.loginIdPref),
                                    ""
                                )!!
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
        }
    }

    override fun onDialogClickMarketing2(value1: String, value2: String) {
        choosenMarketing = value1
        binding.marketing.setText(value2)
    }

}