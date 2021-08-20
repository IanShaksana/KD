package com.example.kd.fragment.marketing.submission.deposito.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.Sub32DepositoDetailFragmentBinding
import com.example.kd.modelbody.IdOnly
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class Sub32DepositoDetail : Fragment() {


    private lateinit var binding: Sub32DepositoDetailFragmentBinding
    private val value: Sub32DepositoDetailArgs by navArgs()
    private lateinit var data: JSONObject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Sub32DepositoDetailFragmentBinding.inflate(inflater, container, false)
        backgroundInitial()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.edit.setOnClickListener {
            Toast.makeText(context, value.idDetailDepositoSubmisson, Toast.LENGTH_SHORT).show()
            val action =
                Sub32DepositoDetailDirections.actionSub02DepositoDetailToSub32DepositoEdit(value.idDetailDepositoSubmisson)
            requireView().findNavController().navigate(action)
        }

        binding.delete.setOnClickListener {
            backgroundDelete()
        }

        binding.finish.setOnClickListener {
            backgroundFinish()
        }
    }


    private fun backgroundInitial() {
        CoroutineScope(Dispatchers.IO).launch {
            onSuccess(
                getDetail(
                    this@Sub32DepositoDetail.requireContext().resources.getString(R.string.submitDepositoGetDetail),
                    JSONObject(
                        Gson().toJson(
                            IdOnly(
                                value.idDetailDepositoSubmisson
                            )
                        )
                    )
                )
            )
        }
    }

    private fun getDetail(url: String, jObject: JSONObject): JSONObject {
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
            this@Sub32DepositoDetail.data = data
            binding.apply {
                pengajuanTitle.text = data.getString("pengajuanTitle")

                deposanNama.text = "Nama : ${data.getString("deposanNama")}"
                deposanKontak.text = "Kontak : ${data.getString("deposanKontak")}"
                deposanAlamat.text = "Alamat : ${data.getString("deposanAlamat")}"
                deposanNik.text = "NIK : ${data.getString("deposanNik")}"

                pengajuanProduk.text = "Produk : ${data.getString("pengajuanProduk")}"
                pengajuanTipe.text = "Tipe : ${data.getString("pengajuanTipe")}"
                pengajuanSaldoAwal.text = "Saldo Awal : ${data.getString("pengajuanSaldoAwal")}"
//                pengajuanJangkaWaktu.text =
//                    "Jangka Waktu : ${data.getString("pengajuanJangkaWaktu")}"
                pengajuanBungaRate.text = "Bunga (%) : ${data.getString("pengajuanBungaRate")}"

                val tanggal = DateTime(data.getString("pengajuanTanggal"))
                val valueTanggal =
                    tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                pengajuanTanggal.text = "Tanggal : ${valueTanggal}"

                status.text = "Status : ${data.getString("status")}"

                informasiTambahan.text = "Detail : ${data.getString("informasiTambahan")}"
                review.text = "Detail : ${data.getString("review")}"

                if (data.getString("status").equals("Draft", true)) {
                    edit.isEnabled = true
                    delete.isEnabled = true
                    finish.isEnabled = true
                } else {
                    edit.isEnabled = false
                    delete.isEnabled = false
                    finish.isEnabled = false
                }

            }


        }
    }

    private fun backgroundDelete() {
        CoroutineScope(Dispatchers.IO).launch {

            onSuccessDelete(
                delete(
                    this@Sub32DepositoDetail.requireContext().resources.getString(R.string.submitDepositoDelete),
                    this@Sub32DepositoDetail.data
                )
            )
        }
    }

    private fun delete(url: String, jObject: JSONObject): JSONObject {
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

    private suspend fun onSuccessDelete(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                binding.root.findNavController().popBackStack()
            }
        }
    }

    private fun backgroundFinish() {
        CoroutineScope(Dispatchers.IO).launch {

            onSuccessFinish(
                finish(
                    this@Sub32DepositoDetail.requireContext().resources.getString(R.string.submitDepositoSend),
                    this@Sub32DepositoDetail.data
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
            //val resp = future.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resp

    }

    private suspend fun onSuccessFinish(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                binding.root.findNavController().popBackStack()
            }
        }
    }


}