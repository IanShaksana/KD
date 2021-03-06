package com.segen.kd.fragment.marketing.submission.loan.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.segen.kd.R
import com.segen.kd.databinding.Sub31LoanDetailFragmentBinding
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.modelbody.IdVersionOnly
import com.segen.kd.utility.GentUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject

class Sub31LoanDetail : Fragment() {
    private lateinit var binding: Sub31LoanDetailFragmentBinding
    private val value: Sub31LoanDetailArgs by navArgs()
    private lateinit var data: JSONObject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Sub31LoanDetailFragmentBinding.inflate(inflater, container, false)
        backgroundInitial()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.edit.setOnClickListener {
            val action =
                Sub31LoanDetailDirections.actionSub01LoanDetailToSub31LoanEdit(value.idDetailLoanSubmission)
            binding.root.findNavController().navigate(action)
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
            val obj = IdOnly(
                value.idDetailLoanSubmission
            )

            onSuccess(
                GentUtil().process(
                    this@Sub31LoanDetail.resources.getString(R.string.submitLoanGetDetail),
                    obj,
                    this@Sub31LoanDetail.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            val data = resp.getJSONArray("data").getJSONObject(0)
            this@Sub31LoanDetail.data = data
            binding.apply {
                pengajuanTitle.text = data.getString("pengajuanTitle")

                debiturNama.text = "Nama : ${data.getString("debiturNama")}"
                debiturKontak.text = "Kontak : ${data.getString("debiturKontak")}"
                debiturAlamat.text = "Alamat : ${data.getString("debiturAlamat")}"
                debiturNik.text = "NIK : ${data.getString("debiturNik")}"

                pengajuanProduk.text = "Produk : ${data.getString("pengajuanProduk")}"
                pengajuanPlafon.text = "Plafon : ${data.getString("pengajuanPlafon")}"
                pengajuanJangkaWaktu.text =
                    "Jangka waktu : ${data.getString("pengajuanJangkaWaktu")}"
                pengajuanBungaRate.text = "Bunga (%) : ${data.getString("pengajuanBungaRate")}"
                pengajuanProvisiRate.text = "Provisi (%) : ${data.getString("pengajuanProvisiRate")}"
                val tanggal1 = DateTime(data.getString("pengajuanTanggal"))
                val valueTanggal1 =
                    tanggal1.toString(activity?.resources?.getString(R.string.format_date_1))
                val tanggal2 = DateTime(data.getString("pengajuanTanggalAngsuranPertama"))
                val valueTanggal2 =
                    tanggal2.toString(activity?.resources?.getString(R.string.format_date_1))
                pengajuanTanggal.text = "Tanggal : ${valueTanggal1}"
                pengajuanTanggalAngsuranPertama.text = "Tanggal angsuran pertama : ${valueTanggal2}"
                pengajuanJenisPenggunaan.text = "Jenis penggunaan : ${data.getString("pengajuanJenisPenggunaan")}"
                pengajuanTujuanKredit.text = "Tujuan kredit : ${data.getString("pengajuanTujuanKredit")}"

                jaminanTipe.text = "Tipe : ${data.getString("jaminanTipe")}"
                jaminanKepemilikan.text = "Kepemelikan : ${data.getString("jaminanKepemilikan")}"
                jaminanTahun.text = "Tahun : ${data.getString("jaminanTahun")}"
                jaminanNominal.text = "Nominal : ${data.getString("jaminanNominal")}"
                jaminanDeskripsi.text = "Deskripsi : ${data.getString("jaminanDeskripsi")}"

                status.text = "Status : ${data.getString("status")}"

                informasiTambahan.text = "Detil : ${data.getString("informasiTambahan")}"
                review.text = "Detil : ${data.getString("review")}"



                if (data.getString("status").equals("Draft", true)) {
                    edit.isEnabled = true
                    delete.isEnabled = true
                    finish.isEnabled = true
                } else {
                    edit.isEnabled = false
                    delete.isEnabled = false
                    finish.isEnabled = false
                }

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

    private fun backgroundDelete() {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdVersionOnly(this@Sub31LoanDetail.data.getString("id"),this@Sub31LoanDetail.data.getInt("version")!!)

            onSuccessDelete(
                GentUtil().process(
                    this@Sub31LoanDetail.resources.getString(R.string.submitLoanDelete),
                    obj,
                    this@Sub31LoanDetail.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccessDelete(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                Toast.makeText(
                    requireContext(),
                    "Pinjaman Berhasil Dihapus",
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.findNavController().popBackStack()
            }
        }
    }

    private fun backgroundFinish() {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdVersionOnly(this@Sub31LoanDetail.data.getString("id"),this@Sub31LoanDetail.data.getInt("version")!!)

            onSuccessFinish(
                GentUtil().process(
                    this@Sub31LoanDetail.resources.getString(R.string.submitLoanSend),
                    obj,
                    this@Sub31LoanDetail.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccessFinish(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                Toast.makeText(
                    requireContext(),
                    "Pinjaman Diajukan",
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.findNavController().popBackStack()
            }
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