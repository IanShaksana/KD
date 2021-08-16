package com.example.kd.fragment.marketing.submission.loan.core

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.Sub31LoanCreateFragmentBinding
import com.example.kd.databinding.Sub32DepositoCreateFragmentBinding
import com.example.kd.dialog.DialogDate
import com.example.kd.dialog.DialogJaminanKepemelikan
import com.example.kd.dialog.DialogJaminanTipe
import com.example.kd.dialog.DialogLoanProduk
import com.example.kd.modelbody.DepositoCreate
import com.example.kd.modelbody.LoanCreate
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Sub31LoanCreate : Fragment(),
    DialogLoanProduk.dialogListener, DialogJaminanKepemelikan.dialogListener,
    DialogJaminanTipe.dialogListener, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: Sub31LoanCreateFragmentBinding
    private var focused: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Sub31LoanCreateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.pengajuanProduk.setOnClickListener {
            val dialog = DialogLoanProduk()
            dialog.show(childFragmentManager, "Dialog Loan Produk")
        }

        binding.jaminanTipe.setOnClickListener {
            val dialog = DialogJaminanTipe()
            dialog.show(childFragmentManager, "Dialog Jaminan Tipe")
        }

        binding.jaminanKepemilikan.setOnClickListener {
            val dialog = DialogJaminanKepemelikan()
            dialog.show(childFragmentManager, "Dialog Jaminan Kepemilikan")
        }

        binding.pengajuanTanggal.setOnClickListener {
            focused = 1
            val dialog = DialogDate()
            dialog.show(childFragmentManager,"")
//            binding.pengajuanTanggal.setText("2020-01-01")
        }

        binding.pengajuanTanggalAngsuranPertama.setOnClickListener {
            focused = 2
            val dialog = DialogDate()
            dialog.show(childFragmentManager,"")
//            binding.pengajuanTanggalAngsuranPertama.setText("2020-01-01")
        }

        binding.create.setOnClickListener {
            var fields: Array<TextInputEditText> = arrayOf(

                binding.pengajuanTitle,
                binding.debiturNama,
                binding.debiturAlamat,
                binding.debiturKontak,
                binding.debiturKontak,

                binding.pengajuanProduk,
                binding.pengajuanPlafon,
                binding.pengajuanBungaRate,
                binding.pengajuanProvisiRate,
                binding.pengajuanJangkaWaktu,
                binding.pengajuanTanggal,
                binding.pengajuanTanggalAngsuranPertama,
                binding.pengajuanJenisPenggunaan,
                binding.pengajuanTujuanKredit,

                binding.jaminanDeskripsi,
                binding.jaminanKepemilikan,
                binding.jaminanNominal,
                binding.jaminanTahun,
                binding.jaminanTipe
            )
            if (validate(fields)) {
                background()
            } else {
                Toast.makeText(requireContext(), "Form Belum Terisi Semua", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDialogClick(value: String) {
        binding.pengajuanProduk.setText(value)
    }

    override fun onDialogJaminanKepemilikanClick(value: String) {
        binding.jaminanKepemilikan.setText(value)
    }

    override fun onDialogJaminanTipeClick(value: String) {
        binding.jaminanTipe.setText(value)
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

    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@Sub31LoanCreate.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )

            onSuccess(
                create(
                    this@Sub31LoanCreate.requireContext().resources.getString(R.string.submitLoanCreate),
                    JSONObject(
                        Gson().toJson(
                            LoanCreate(
                                sharedPref.getString(
                                    getString(R.string.loginIdPref),
                                    ""
                                ),
                                binding.pengajuanTitle.text.toString(),

                                binding.debiturNama.text.toString(),
                                binding.debiturKontak.text.toString(),
                                binding.debiturAlamat.text.toString(),
                                binding.debiturNik.text.toString(),

                                binding.pengajuanProduk.text.toString(),
                                binding.pengajuanPlafon.text.toString(),
                                binding.pengajuanJangkaWaktu.text.toString(),
                                binding.pengajuanBungaRate.text.toString(),
                                binding.pengajuanProvisiRate.text.toString(),
                                binding.pengajuanTanggal.text.toString(),
                                binding.pengajuanTanggalAngsuranPertama.text.toString(),
                                binding.pengajuanJenisPenggunaan.text.toString(),
                                binding.pengajuanTujuanKredit.text.toString(),

                                binding.jaminanTipe.text.toString(),
                                binding.jaminanKepemilikan.text.toString(),
                                binding.jaminanTahun.text.toString(),
                                binding.jaminanNominal.text.toString(),
                                binding.jaminanDeskripsi.text.toString(),

                                binding.informasiTambahan.text.toString()
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

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val c = Calendar.getInstance()
        c[Calendar.YEAR] = p1
        c[Calendar.MONTH] = p2
        c[Calendar.DAY_OF_MONTH] = p3
        val format1 = SimpleDateFormat(requireContext().resources.getString(R.string.format_date_1))
        val currentDate = format1.format(c.time)
        if(focused ==1){
            binding.pengajuanTanggal.setText(currentDate)
        }
        if (focused == 2){
            binding.pengajuanTanggalAngsuranPertama.setText(currentDate)
        }
        focused = 0
    }

}