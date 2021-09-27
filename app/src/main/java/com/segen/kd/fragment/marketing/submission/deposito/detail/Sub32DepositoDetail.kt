package com.segen.kd.fragment.marketing.submission.deposito.detail

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
import com.segen.kd.databinding.Sub32DepositoDetailFragmentBinding
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.modelbody.IdVersionOnly
import com.segen.kd.utility.GentUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject

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
           val obj =  IdOnly(
                value.idDetailDepositoSubmisson
            )
            onSuccess(
                GentUtil().process(
                    this@Sub32DepositoDetail.resources.getString(R.string.submitDepositoGetDetail),
                    obj,
                    this@Sub32DepositoDetail.requireContext()
                )
            )
        }
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
                pengajuanSaldoAwal.text = "Saldo awal : ${data.getString("pengajuanSaldoAwal")}"
//                pengajuanJangkaWaktu.text =
//                    "Jangka Waktu : ${data.getString("pengajuanJangkaWaktu")}"
                pengajuanBungaRate.text = "Bunga (%) : ${data.getString("pengajuanBungaRate")}"

                val tanggal = DateTime(data.getString("pengajuanTanggal"))
                val valueTanggal =
                    tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                pengajuanTanggal.text = "Tanggal : ${valueTanggal}"

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
                    binding.deposanNama,
                    binding.deposanKontak,
                    binding.deposanAlamat,
                    binding.deposanNik,
                    binding.pengajuanProduk,

                    binding.pengajuanTipe,
                    binding.pengajuanSaldoAwal,
                    binding.pengajuanBungaRate,
                    binding.pengajuanTanggal,

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
            val obj = IdVersionOnly(this@Sub32DepositoDetail.data.getString("id"),this@Sub32DepositoDetail.data.getInt("version")!!)
            onSuccessDelete(
                GentUtil().process(
                    this@Sub32DepositoDetail.resources.getString(R.string.submitDepositoDelete),
                    obj,
                    this@Sub32DepositoDetail.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccessDelete(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                Toast.makeText(
                    requireContext(),
                    "Deposito Berhasil Dihapus",
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.findNavController().popBackStack()
            }else{
                Toast.makeText(
                    requireContext(),
                    resp["message"].toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun backgroundFinish() {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdVersionOnly(this@Sub32DepositoDetail.data.getString("id"),this@Sub32DepositoDetail.data.getInt("version")!!)
            onSuccessFinish(
                GentUtil().process(
                    this@Sub32DepositoDetail.resources.getString(R.string.submitDepositoSend),
                    obj,
                    this@Sub32DepositoDetail.requireContext()
                )
            )


        }
    }

    private suspend fun onSuccessFinish(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                Toast.makeText(
                    requireContext(),
                    "Deposito Diajukan",
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