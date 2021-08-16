package com.example.kd.fragment.marketing.task.collection.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.Frag21Collection02DetailBinding
import com.example.kd.dialog.DialogInputMarketing
import com.example.kd.modelbody.IdMessageOnly
import com.example.kd.modelbody.IdOnly
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class Task01CollectionDetail : Fragment(), DialogInputMarketing.dialogListener {

    private lateinit var viewModel: Task01CollectionDetailViewModel
    private lateinit var binding: Frag21Collection02DetailBinding
    private val value: Task01CollectionDetailArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Frag21Collection02DetailBinding.inflate(inflater, container, false)
        background()
        binding.finish.setOnClickListener {
            val dialog = DialogInputMarketing()
            dialog.show(childFragmentManager, "Dialog Input Marketing")
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Task01CollectionDetailViewModel::class.java)
        val arg = value.value
        viewModel.initial(arg, activity)
        // TODO: Use the ViewModel
    }

    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {

            onSuccess(
                getTask(
                    this@Task01CollectionDetail.requireContext().resources.getString(R.string.getTaskDetail),
                    JSONObject(
                        Gson().toJson(
                            IdOnly(
                                value.value
                            )
                        )
                    )
                )
            )
        }
    }

    private fun getTask(url: String, jObject: JSONObject): JSONObject {
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
            val data = resp.getJSONArray("data").getJSONObject(0)
            binding.apply {
                noPK.text = "No PK : ${data.getString("noPK")}"

                anggotaNama.text = "Nama : ${data.getString("anggotaNama")}"
                anggotaAlamat.text = "Alamat : ${data.getString("anggotaAlamat")}"
                anggotaKontak.text = "Kontak : ${data.getString("anggotaKontak")}"

                val tanggal = DateTime(data.getString("angsuranTanggal"))
                val valueTanggal =
                    tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                angsuranTanggal.text = "Tanggal : $valueTanggal"
                angsuranNominal.text = "Nominal : ${data.getString("angsuranNominal")}"
                angsuranDenda.text = "Denda : ${data.getString("angsuranDenda")}"

                review.text = "Detail : ${data.getString("review")}"
                noteMarketing.text = "Detail : ${data.getString("noteMarketing")}"

                status.text = "Status : ${data.getString("status")}"

                finish.isEnabled = data.getString("status").equals("To Do", true)
                if (finish.isEnabled) {
                    finish.visibility = View.VISIBLE
                } else {
                    finish.visibility = View.INVISIBLE

                }
            }


        }
    }

    private fun backgroundFinish(message: String) {
        CoroutineScope(Dispatchers.IO).launch {

            onFinish(
                finish(
                    this@Task01CollectionDetail.requireContext().resources.getString(R.string.finishTask),
                    JSONObject(
                        Gson().toJson(
                            IdMessageOnly(
                                value.value, message
                            )
                        )
                    )
                )
            )
        }
    }

    private fun finish(url: String, jObject: JSONObject): JSONObject {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(this.requireContext())
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)

        var resp = JSONObject()
        try {
            resp = future.get(15, TimeUnit.SECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resp

    }

    private suspend fun onFinish(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                // update fragment
                binding.root.findNavController().popBackStack()
            }
        }
    }

    override fun onDialogInputMarketingClick(value: String) {
        backgroundFinish(value)
    }


}