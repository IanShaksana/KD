package com.example.kd.fragment.manager.submission.deposito.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.FragmentDepositoManageDetailBinding
import com.example.kd.databinding.Sub32DepositoDetailFragmentBinding
import com.example.kd.dialog.DialogFinishReject
import com.example.kd.dialog.DialogInputMarketing
import com.example.kd.fragment.marketing.submission.deposito.detail.Sub32DepositoDetailArgs
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


class DepositoManageDetail : Fragment(), DialogFinishReject.dialogListenerFinishReject {

    private lateinit var binding: FragmentDepositoManageDetailBinding
    private val value: DepositoManageDetailArgs by navArgs()
    private lateinit var data: JSONObject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDepositoManageDetailBinding.inflate(inflater, container, false)
        backgroundInitial()
        binding.finish.setOnClickListener {
            val dialog = DialogFinishReject()
            dialog.show(childFragmentManager, "Dialog Input Marketing")
        }
        return binding.root
    }

    private fun backgroundInitial() {
        CoroutineScope(Dispatchers.IO).launch {
            onSuccess(
                getDetail(
                    this@DepositoManageDetail.requireContext().resources.getString(R.string.submitDepositoGetDetailManager),
                    JSONObject(
                        Gson().toJson(
                            IdOnly(
                                value.idsub
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
            this@DepositoManageDetail.data = data
            binding.apply {
                pengajuanTitle.text = data.getString("pengajuanTitle")

                deposanNama.text = "Nama : ${data.getString("deposanNama")}"
                deposanKontak.text = "Kontak : ${data.getString("deposanKontak")}"
                deposanAlamat.text = "Alamat : ${data.getString("deposanAlamat")}"
                deposanNik.text = "NIK : ${data.getString("deposanNik")}"

                pengajuanProduk.text = "Produk : ${data.getString("pengajuanProduk")}"
                pengajuanTipe.text = "Tipe : ${data.getString("pengajuanTipe")}"
                pengajuanSaldoAwal.text = "Saldo Awal : ${data.getString("pengajuanSaldoAwal")}"
                pengajuanJangkaWaktu.text =
                    "Jangka Waktu : ${data.getString("pengajuanJangkaWaktu")}"
                pengajuanBungaRate.text = "Bunga (%) : ${data.getString("pengajuanBungaRate")}"

                val tanggal = DateTime(data.getString("pengajuanTanggal"))
                val valueTanggal =
                    tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                pengajuanTanggal.text = "Tanggal : ${valueTanggal}"

                status.text = "Status : ${data.getString("status")}"

                informasiTambahan.text = "Detail : ${data.getString("informasiTambahan")}"
                review.text = "Detail : ${data.getString("review")}"

                finish.isEnabled = data.getString("status").equals("Diajukan", true)

            }


        }
    }

    private fun backgroundFinish(message: String, choice: Int) {
        CoroutineScope(Dispatchers.IO).launch {

            if(choice ==1){
                onFinish(
                    finish(
                        this@DepositoManageDetail.requireContext().resources.getString(R.string.submitDepositoFinishManager),
                        JSONObject(
                            Gson().toJson(
                                IdMessageOnly(
                                    value.idsub, message
                                )
                            )
                        )
                    )
                )
            }

            if(choice ==0){
                onFinish(
                    finish(
                        this@DepositoManageDetail.requireContext().resources.getString(R.string.submitDepositoRejectManager),
                        JSONObject(
                            Gson().toJson(
                                IdMessageOnly(
                                    value.idsub, message
                                )
                            )
                        )
                    )
                )
            }

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


    override fun onDialogClickFinishReject(value: String, choice: Int) {
        backgroundFinish(value, choice)
    }

}