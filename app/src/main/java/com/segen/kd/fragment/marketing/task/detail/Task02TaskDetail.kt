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
import com.segen.kd.databinding.Frag22Collection02DetailBinding
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
import java.util.*

class Task02TaskDetail : Fragment(), DialogInputMarketing.dialogListener {


    private lateinit var binding: Frag22Collection02DetailBinding
    private val value: Task02TaskDetailArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = Frag22Collection02DetailBinding.inflate(inflater, container, false)
        background()
        binding.fileCard.setOnClickListener {
            val action =
                Task02TaskDetailDirections.actionTask02TaskDetailToFile()
            binding.root.findNavController().navigate(action)
        }
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
                    this@Task02TaskDetail.resources.getString(R.string.getTaskDetail),
                    obj,
                    this@Task02TaskDetail.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            val data = resp.getJSONArray("data").getJSONObject(0)
            binding.apply {
                detailSumber.text = "Dibuat oleh : ${data.getString("detailSumber")}"

                val tanggal = DateTime(data.getString("createdat"))
                val deadline = DateTime(data.getString("detailDeadline"))
                val valueTanggal =
                    tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                val valueDeadline =
                    deadline.toString(activity?.resources?.getString(R.string.format_date_1))

                detailTanggal.text = "Dibuat tanggal : $valueTanggal"
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

                fileJumlah.text = "Jumlah file : ${data.getInt("fileJumlah")}"

                review.text = "Detil : ${data.getString("review")}"
                noteMarketing.text = "Detil : ${data.getString("noteMarketing")}"

                finish.isEnabled = data.getString("status").equals("To Do", true)
                if (finish.isEnabled) {
                    finish.visibility = View.VISIBLE
                } else {
                    finish.visibility = View.INVISIBLE
                }

                var fields: Array<TextView> = arrayOf(
                    binding.detailSumber,
                    binding.detailTanggal,
                    binding.detailTanggal,
                    binding.detailDesc,
                    binding.detailFinishDate,
                    binding.fileJumlah,
                    binding.review,
                    binding.noteMarketing
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
                    this@Task02TaskDetail.resources.getString(R.string.finishTask),
                    obj,
                    this@Task02TaskDetail.requireContext()
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
                currentField.text = value + "-"
            }
        }
        return true
    }


}