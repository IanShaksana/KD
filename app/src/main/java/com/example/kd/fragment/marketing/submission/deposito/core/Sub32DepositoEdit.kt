package com.example.kd.fragment.marketing.submission.deposito.core

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.Sub32DepositoEditFragmentBinding
import com.example.kd.dialog.DialogDate
import com.example.kd.dialog.DialogDepositoProduk
import com.example.kd.dialog.DialogDepositoTipe
import com.example.kd.modelbody.DepositoEdit
import com.example.kd.modelbody.IdOnly
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import com.google.android.material.textfield.TextInputEditText
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.text.SimpleDateFormat
import java.util.*


class Sub32DepositoEdit : Fragment(),
    DialogDepositoProduk.dialogListener, DialogDepositoTipe.dialogListener, DatePickerDialog.OnDateSetListener {


    private lateinit var binding: Sub32DepositoEditFragmentBinding
    private val value: Sub32DepositoEditArgs by navArgs()
    private lateinit var data: JSONObject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Sub32DepositoEditFragmentBinding.inflate(inflater, container, false)
        backgroundInitial()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.pengajuanProduk.setOnClickListener {
            val dialog = DialogDepositoProduk()
            dialog.show(childFragmentManager, "Dialog Deposito Produk")
        }

        binding.pengajuanTipe.setOnClickListener {
            val dialog = DialogDepositoTipe()
            dialog.show(childFragmentManager, "Dialog Deposito Tipe")
        }

        binding.pengajuanTanggal.setOnClickListener {
            val dialog = DialogDate()
            dialog.show(childFragmentManager,"")
//            binding.pengajuanTanggal.setText("2020-01-01")
        }

        binding.edit.setOnClickListener {
            var fields: Array<TextInputEditText> = arrayOf(
                binding.deposanNama,
                binding.deposanAlamat,
                binding.deposanKontak,
                binding.deposanNik,
                binding.pengajuanBungaRate,
                // binding.pengajuanJangkaWaktu,
                binding.pengajuanProduk,
                binding.pengajuanSaldoAwal,
                binding.pengajuanTanggal,
                binding.pengajuanTipe,
                binding.pengajuanTitle
            )
            if (validate(fields)) {
                backgroundEdit()
            }else{
                Toast.makeText(
                    requireContext(),
                    "Ada Bagian Yang Belum Terisi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDialogDepositoProdukClick(value: String) {
        binding.pengajuanProduk.setText(value)
    }

    override fun onDialogDepositoTipeClick(value: String) {
        binding.pengajuanTipe.setText(value)
    }

    private fun backgroundInitial() {
        CoroutineScope(Dispatchers.IO).launch {
            onSuccess(
                getDetail(
                    this@Sub32DepositoEdit.requireContext().resources.getString(R.string.submitDepositoGetDetail),
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
            this@Sub32DepositoEdit.data = data
            binding.apply {
                pengajuanTitle.setText(data.getString("pengajuanTitle"))

                deposanNama.setText("${data.getString("deposanNama")}")
                deposanKontak.setText("${data.getString("deposanKontak")}")
                deposanAlamat.setText("${data.getString("deposanAlamat")}")
                deposanNik.setText("${data.getString("deposanNik")}")

                pengajuanProduk.setText("${data.getString("pengajuanProduk")}")
                pengajuanTipe.setText("${data.getString("pengajuanTipe")}")
                pengajuanSaldoAwal.setText("${data.getString("pengajuanSaldoAwal")}")
//                pengajuanJangkaWaktu.setText(
//                    "${data.getString("pengajuanJangkaWaktu")}"
//                )
                pengajuanBungaRate.setText("${data.getString("pengajuanBungaRate")}")
                val tanggal = DateTime(data.getString("pengajuanTanggal"))
                val valueTanggal =
                    tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                pengajuanTanggal.setText("${valueTanggal}")

                informasiTambahan.setText(
                    "${data.getString("informasiTambahan")}"
                )
            }

        }


    }

    private fun backgroundEdit() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@Sub32DepositoEdit.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )

            onSuccessEdit(
                edit(
                    this@Sub32DepositoEdit.requireContext().resources.getString(R.string.submitDepositoCreate),
                    JSONObject(
                        Gson().toJson(
                            DepositoEdit(
                                value.idDetailDepositoSubmisson,
                                sharedPref.getString(
                                    getString(R.string.loginIdPref),
                                    ""
                                ),
                                binding.pengajuanTitle.text.toString(),
                                binding.deposanNama.text.toString(),
                                binding.deposanKontak.text.toString(),
                                binding.deposanAlamat.text.toString(),
                                binding.deposanNik.text.toString(),
                                binding.pengajuanProduk.text.toString(),
                                binding.pengajuanTipe.text.toString(),
                                binding.pengajuanSaldoAwal.text.toString(),
                                binding.pengajuanBungaRate.text.toString(),
                                binding.pengajuanTanggal.text.toString(),
                                binding.informasiTambahan.text.toString(),
                                data.getInt("version")
                            )
                        )
                    )
                )
            )
        }
    }

    private fun edit(url: String, jObject: JSONObject): JSONObject {
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

    private suspend fun onSuccessEdit(resp: JSONObject) {
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
            binding.pengajuanTanggal.setText(currentDate)
        } else {
            Toast.makeText(
                requireContext(),
                "Tanggal Tidak Boleh Kurang Dari Tanggal Hari Ini",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}


