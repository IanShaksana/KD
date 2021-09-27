package com.segen.kd.fragment.marketing.task.detail

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
import com.segen.kd.databinding.Frag21Collection02DetailBinding
import com.segen.kd.dialog.DialogInputMarketing
import com.segen.kd.modelbody.IdMessageOnly
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.utility.GentUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject

class Task01CollectionDetail : Fragment(), DialogInputMarketing.dialogListener {

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

    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdOnly(
                value.value
            )

            onSuccess(
                GentUtil().process(
                    this@Task01CollectionDetail.resources.getString(R.string.getTaskDetail),
                    obj,
                    this@Task01CollectionDetail.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            val data = resp.getJSONArray("data").getJSONObject(0)
            binding.apply {
                noPK.text = "No PK : ${data.getString("noPK")}"

                detailSumber.text = "Dibuat oleh : ${data.getString("detailSumber")}"
                anggotaNama.text = "Nama : ${data.getString("anggotaNama")}"
                anggotaAlamat.text = "Alamat : ${data.getString("anggotaAlamat")}"
                anggotaKontak.text = "Kontak : ${data.getString("anggotaKontak")}"

                val tanggal = DateTime(data.getString("angsuranTanggal"))
                val valueTanggal =
                    tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                angsuranTanggal.text = "Tanggal : $valueTanggal"
                angsuranNominal.text = "Nominal : ${data.getString("angsuranNominal")}"
                angsuranDenda.text = "Denda : ${data.getString("angsuranDenda")}"

                review.text = "Detil : ${data.getString("review")}"
                noteMarketing.text = "Detil : ${data.getString("noteMarketing")}"

                fasilitasDenda.text = "Total denda : ${data.getString("nominaldenda")}"
                fasilitasStatusDenda.text = "Status denda : ${data.getString("flagdenda")}"

                val tanggal0 = DateTime(data.getString("createdat"))
                val deadline = DateTime(data.getString("detailDeadline"))
                val valueTanggal0 =
                    tanggal0.toString(activity?.resources?.getString(R.string.format_date_1))
                val valueDeadline =
                    deadline.toString(activity?.resources?.getString(R.string.format_date_1))

                detailTanggal.text = "Dibuat tanggal : $valueTanggal0"
                detailDeadline.text = "Deadline : $valueDeadline"
                detailDesc.text = "Deskripsi : ${data.getString("detailDesc")}"

                status.text = "Status : ${data.getString("status")}"
                val status: String = data.getString("status")
                if (status.equals("Review", true) || status.equals(
                        "Selesai",
                        true
                    ) || status.equals("Ditolak", true)
                ) {
                    val finish = DateTime(data.getString("finishdate"))
                    val valuefinish =
                        finish.toString(activity?.resources?.getString(R.string.format_date_1))
                    detailFinishDate.text = "Tanggal selesai : ${valuefinish}"
                } else {
                    detailFinishDate.text = "Tanggal selesai :"
                }

                finish.isEnabled = data.getString("status").equals("To Do", true)
                if (finish.isEnabled) {
                    finish.visibility = View.VISIBLE
                } else {
                    finish.visibility = View.INVISIBLE

                }


                var fields: Array<TextView> = arrayOf(
                    binding.noPK,
                    binding.detailSumber,
                    binding.anggotaNama,
                    binding.anggotaAlamat,
                    binding.anggotaKontak,
                    binding.angsuranTanggal,
                    binding.angsuranNominal,
                    binding.angsuranDenda,
                    binding.review,
                    binding.noteMarketing,
                    binding.detailTanggal,
                    binding.detailDeadline,
                    binding.detailDesc,
                    binding.status,
                    binding.detailFinishDate,
                )
                validate(fields)
            }


        }
    }

    private fun backgroundFinish(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdMessageOnly(
                value.value, message
            )

            onFinish(
                GentUtil().process(
                    this@Task01CollectionDetail.resources.getString(R.string.finishTask),
                    obj,
                    this@Task01CollectionDetail.requireContext()
                )
            )

        }
    }

    private suspend fun onFinish(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                // update fragment
                Toast.makeText(
                    requireContext(),
                    "Tugas Siap Direview",
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.findNavController().popBackStack()
            }
        }
    }

    override fun onDialogInputMarketingClick(value: String) {
        backgroundFinish(value)
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