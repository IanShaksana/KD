package com.segen.kd.fragment.manager.submission.deposito.detail

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
import com.segen.kd.databinding.FragmentDepositoManageDetailBinding
import com.segen.kd.dialog.DialogFinishReject
import com.segen.kd.modelbody.IdMessageOnly
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.utility.GentUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject

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
            val obj = IdOnly(
                value.idsub
            )

            onSuccess(
                GentUtil().process(
                    this@DepositoManageDetail.resources.getString(R.string.submitDepositoGetDetailManager),
                    obj,
                    this@DepositoManageDetail.requireContext()
                )
            )
        }
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
                pengajuanBungaRate.text = "Bunga (%) : ${data.getString("pengajuanBungaRate")}"

                val tanggal = DateTime(data.getString("pengajuanTanggal"))
                val valueTanggal =
                    tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                pengajuanTanggal.text = "Tanggal : ${valueTanggal}"

                status.text = "Status : ${data.getString("status")}"

                informasiTambahan.text = "Detail : ${data.getString("informasiTambahan")}"
                review.text = "Detail : ${data.getString("review")}"

                finish.isEnabled = data.getString("status").equals("Diajukan", true)

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

    private fun backgroundFinish(message: String, choice: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdMessageOnly(
                value.idsub, message
            )

            if (choice == 1) {
                onFinish(
                    GentUtil().process(
                        this@DepositoManageDetail.resources.getString(R.string.submitDepositoFinishManager),
                        obj,
                        this@DepositoManageDetail.requireContext()
                    )
                )
            }

            if (choice == 0) {
                onFinish(
                    GentUtil().process(
                        this@DepositoManageDetail.resources.getString(R.string.submitDepositoRejectManager),
                        obj,
                        this@DepositoManageDetail.requireContext()
                    )
                )
            }

        }
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
                currentField.text = value +"-"
            }
        }
        return true
    }

}