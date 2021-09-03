package com.example.kd.fragment.manager.submission.loan.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.FragmentLoanManageDetailBinding
import com.example.kd.dialog.DialogFinishReject
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


class LoanManageDetail : Fragment(), DialogFinishReject.dialogListenerFinishReject {

    private lateinit var binding: FragmentLoanManageDetailBinding
    private val value: LoanManageDetailArgs by navArgs()
    private lateinit var data: JSONObject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoanManageDetailBinding.inflate(inflater, container, false)
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
                    this@LoanManageDetail.requireContext().resources.getString(R.string.submitLoanGetDetailManager),
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
            this@LoanManageDetail.data = data
            binding.apply {
                pengajuanTitle.text = data.getString("pengajuanTitle")

                debiturNama.text = "Nama : ${data.getString("debiturNama")}"
                debiturKontak.text = "Kontak : ${data.getString("debiturKontak")}"
                debiturAlamat.text = "Alamat : ${data.getString("debiturAlamat")}"
                debiturNik.text = "NIK : ${data.getString("debiturNik")}"

                pengajuanProduk.text = "Produk : ${data.getString("pengajuanProduk")}"
                pengajuanPlafon.text = "Plafon : ${data.getString("pengajuanPlafon")}"
                pengajuanJangkaWaktu.text =
                    "Jangka Waktu : ${data.getString("pengajuanJangkaWaktu")}"
                pengajuanBungaRate.text = "Bunga (%) : ${data.getString("pengajuanBungaRate")}"
                pengajuanProvisiRate.text =
                    "Provisi (%) : ${data.getString("pengajuanProvisiRate")}"
                val tanggal1 = DateTime(data.getString("pengajuanTanggal"))
                val valueTanggal1 =
                    tanggal1.toString(activity?.resources?.getString(R.string.format_date_1))
                val tanggal2 = DateTime(data.getString("pengajuanTanggalAngsuranPertama"))
                val valueTanggal2 =
                    tanggal2.toString(activity?.resources?.getString(R.string.format_date_1))
                pengajuanTanggal.text = "Tanggal : ${valueTanggal1}"
                pengajuanTanggalAngsuranPertama.text = "Tanggal Angsuran Pertama : ${valueTanggal2}"
                pengajuanJenisPenggunaan.text =
                    "Jenis Penggunaan : ${data.getString("pengajuanJenisPenggunaan")}"
                pengajuanTujuanKredit.text =
                    "Tujuan Kredit : ${data.getString("pengajuanTujuanKredit")}"

                jaminanTipe.text = "Tipe : ${data.getString("jaminanTipe")}"
                jaminanKepemilikan.text = "Kepemelikan : ${data.getString("jaminanKepemilikan")}"
                jaminanTahun.text = "Tahun : ${data.getString("jaminanTahun")}"
                jaminanNominal.text = "Nominal : ${data.getString("jaminanNominal")}"
                jaminanDeskripsi.text = "Deskripsi : ${data.getString("jaminanDeskripsi")}"

                status.text = "Status : ${data.getString("status")}"

                informasiTambahan.text = "Detail : ${data.getString("informasiTambahan")}"
                review.text = "Detail : ${data.getString("review")}"



                finish.isEnabled = data.getString("status").equals("Diajukan", true)

                var fields: Array<TextView> = arrayOf(
                    binding.pengajuanTitle,
                    binding.debiturNama,
                    binding.debiturKontak,
                    binding.debiturAlamat,
                    binding.debiturNik,
                    binding.pengajuanProduk,

                    binding.pengajuanPlafon,
                    binding.pengajuanJangkaWaktu,
                    binding.pengajuanBungaRate,
                    binding.pengajuanProvisiRate,
                    binding.pengajuanTanggal,
                    binding.pengajuanTanggalAngsuranPertama,
                    binding.pengajuanJenisPenggunaan,
                    binding.pengajuanTujuanKredit,

                    binding.jaminanTipe,
                    binding.jaminanKepemilikan,
                    binding.jaminanTahun,
                    binding.jaminanNominal,
                    binding.jaminanDeskripsi,
                    binding.status,
                    binding.review,
                    binding.informasiTambahan
                )
                validate(fields)

            }


        }
    }

    private fun backgroundFinish(message: String, choice: Int) {
        CoroutineScope(Dispatchers.IO).launch {

            if (choice == 1) {
                onFinish(
                    finish(
                        this@LoanManageDetail.requireContext().resources.getString(R.string.submitLoanFinishManager),
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

            if (choice == 0) {
                onFinish(
                    finish(
                        this@LoanManageDetail.requireContext().resources.getString(R.string.submitLoanRejectManager),
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
                Toast.makeText(
                    requireContext(),
                    "Deposito Berhasil Direview",
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.findNavController().popBackStack()
            }
        }
    }

    override fun onDialogClickFinishReject(value: String, choice: Int) {
        backgroundFinish(value, choice)
    }

    private fun validate(fields: Array<TextView>): Boolean {
        for (i in fields.indices) {
            val currentField = fields[i]

            if (currentField.text.toString().contains("null")) {
                val value = currentField.text.toString().split("null").get(0)
                currentField.text = value + "-"
            }
        }
        return true
    }

}