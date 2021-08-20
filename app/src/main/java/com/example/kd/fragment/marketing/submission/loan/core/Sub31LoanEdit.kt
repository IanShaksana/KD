package com.example.kd.fragment.marketing.submission.loan.core

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
import com.example.kd.databinding.Sub31LoanEditFragmentBinding
import com.example.kd.dialog.DialogDate
import com.example.kd.dialog.DialogJaminanKepemelikan
import com.example.kd.dialog.DialogJaminanTipe
import com.example.kd.dialog.DialogLoanProduk
import com.example.kd.modelbody.IdOnly
import com.example.kd.modelbody.LoanEdit
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Sub31LoanEdit : Fragment(),
    DialogLoanProduk.dialogListener, DialogJaminanKepemelikan.dialogListener,
    DialogJaminanTipe.dialogListener, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: Sub31LoanEditFragmentBinding
    private val value: Sub31LoanEditArgs by navArgs()
    private lateinit var data: JSONObject
    private var focused: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Sub31LoanEditFragmentBinding.inflate(inflater, container, false)
        backgroundInitial()
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
            dialog.show(childFragmentManager, "")
//            binding.pengajuanTanggal.setText("2020-01-01")
        }

        binding.pengajuanTanggalAngsuranPertama.setOnClickListener {
            var fields: Array<TextInputEditText> = arrayOf(
                binding.pengajuanTanggal,
            )
            if (validate(fields)) {
                focused = 2
                val dialog = DialogDate()
                dialog.show(childFragmentManager, "")
//            binding.pengajuanTanggalAngsuranPertama.setText("2020-01-01")
            } else {
                Toast.makeText(
                    requireContext(),
                    "Isi Tanggal Diajukan Terlebih Dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        binding.edit.setOnClickListener {
            val fields: Array<TextInputEditText> = arrayOf(

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
                backgroundEdit()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ada Bagian Yang Belum Terisi",
                    Toast.LENGTH_SHORT
                ).show()
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


    private fun backgroundInitial() {
        CoroutineScope(Dispatchers.IO).launch {
            onSuccess(
                getDetail(
                    this@Sub31LoanEdit.requireContext().resources.getString(R.string.submitLoanGetDetail),
                    JSONObject(
                        Gson().toJson(
                            IdOnly(
                                value.idDetailLoanSubmisson
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
            this@Sub31LoanEdit.data = data
            binding.apply {
                pengajuanTitle.setText(data.getString("pengajuanTitle"))

                debiturNama.setText("${data.getString("debiturNama")}")
                debiturKontak.setText("${data.getString("debiturKontak")}")
                debiturAlamat.setText("${data.getString("debiturAlamat")}")
                debiturNik.setText("${data.getString("debiturNik")}")

                pengajuanProduk.setText("${data.getString("pengajuanProduk")}")
                pengajuanPlafon.setText("${data.getString("pengajuanPlafon")}")
                pengajuanBungaRate.setText("${data.getString("pengajuanBungaRate")}")
                pengajuanProvisiRate.setText("${data.getString("pengajuanProvisiRate")}")
                pengajuanJangkaWaktu.setText(
                    "${data.getString("pengajuanJangkaWaktu")}"
                )
                val tanggal1 = DateTime(data.getString("pengajuanTanggal"))
                val valueTanggal1 =
                    tanggal1.toString(activity?.resources?.getString(R.string.format_date_1))
                val tanggal2 = DateTime(data.getString("pengajuanTanggalAngsuranPertama"))
                val valueTanggal2 =
                    tanggal2.toString(activity?.resources?.getString(R.string.format_date_1))
                pengajuanTanggal.setText("${valueTanggal1}")
                pengajuanTanggalAngsuranPertama.setText("${valueTanggal2}")
                pengajuanJenisPenggunaan.setText("${data.getString("pengajuanJenisPenggunaan")}")
                pengajuanTujuanKredit.setText("${data.getString("pengajuanTujuanKredit")}")

                jaminanDeskripsi.setText("${data.getString("jaminanDeskripsi")}")
                jaminanKepemilikan.setText("${data.getString("jaminanKepemilikan")}")
                jaminanNominal.setText("${data.getString("jaminanNominal")}")
                jaminanTahun.setText("${data.getString("jaminanTahun")}")
                jaminanTipe.setText("${data.getString("jaminanTipe")}")

                informasiTambahan.setText(
                    "${data.getString("informasiTambahan")}"
                )
            }

        }


    }

    private fun backgroundEdit() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = this@Sub31LoanEdit.requireActivity().getSharedPreferences(
                getString(R.string.credPref), Context.MODE_PRIVATE
            )

            onSuccessEdit(
                edit(
                    this@Sub31LoanEdit.requireContext().resources.getString(R.string.submitLoanEdit),
                    JSONObject(
                        Gson().toJson(
                            LoanEdit(
                                value.idDetailLoanSubmisson,
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
        if (focused == 1) {
            val dt = LocalDate(c.time)
            val today = LocalDate(Date())
            if (!dt.isBefore(today)) {
                binding.pengajuanTanggal.setText(currentDate)
                binding.pengajuanTanggalAngsuranPertama.text = null
            } else {
                Toast.makeText(
                    requireContext(),
                    "Tanggal Tidak Boleh Kurang Dari Tanggal Hari Ini",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (focused == 2) {
            val dt = LocalDate(c.time)
            val today = DateTimeFormat.forPattern("dd/MM/yyyy")
                .parseDateTime(binding.pengajuanTanggal.text.toString()).toLocalDate()

            if (!dt.isBefore(today)) {
                binding.pengajuanTanggalAngsuranPertama.setText(currentDate)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Tanggal Tidak Boleh Kurang Dari Tanggal Pengajuan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        focused = 0
    }


}