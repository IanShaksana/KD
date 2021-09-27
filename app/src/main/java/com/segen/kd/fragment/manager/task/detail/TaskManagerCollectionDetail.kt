package com.segen.kd.fragment.manager.task.detail

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
import com.segen.kd.databinding.FragmentTaskManagerCollectionDetailBinding
import com.segen.kd.dialog.DialogFinishReject
import com.segen.kd.modelbody.IdMessageOnly
import com.segen.kd.modelbody.IdOnly
import com.segen.kd.modelbody.IdVersionOnly
import com.segen.kd.utility.GentUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject
import timber.log.Timber

class TaskManagerCollectionDetail : Fragment(), DialogFinishReject.dialogListenerFinishReject {

    private lateinit var binding: FragmentTaskManagerCollectionDetailBinding
    private val value: TaskManagerCollectionDetailArgs by navArgs()
    private lateinit var data: JSONObject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTaskManagerCollectionDetailBinding.inflate(inflater, container, false)
        backgroundInitial()
        binding.edit.setOnClickListener {
            val action =
                TaskManagerCollectionDetailDirections.actionTaskManagerCollectionDetailToTaskManagerCollectionEdit(
                    value.idtask
                )
            binding.root.findNavController().navigate(action)
        }
        binding.delete.setOnClickListener {
            Timber.i(data.toString())
            backgroundDelete()
        }
        binding.send.setOnClickListener {
            backgroundSend()
        }
        binding.finish.setOnClickListener {
            val dialog = DialogFinishReject()
            dialog.show(childFragmentManager, "Dialog Input Marketing")
        }
        return binding.root
    }

    private fun backgroundInitial() {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdOnly(
                value.idtask
            )

            onSuccess(
                GentUtil().process(
                    this@TaskManagerCollectionDetail.resources.getString(R.string.getTaskDetailManager),
                    obj,
                    this@TaskManagerCollectionDetail.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccess(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            val data = resp.getJSONArray("data").getJSONObject(0)
            this@TaskManagerCollectionDetail.data = data
            Timber.i(data.toString())
            binding.apply {

                val tanggal0 = DateTime(data.getString("createdat"))
                val deadline = DateTime(data.getString("detailDeadline"))
                val valueTanggal0 =
                    tanggal0.toString(activity?.resources?.getString(R.string.format_date_1))
                val valueDeadline =
                    deadline.toString(activity?.resources?.getString(R.string.format_date_1))

                status.text = "Status : ${data.getString("status")}"
                val status: String = data.getString("status")
                detailSumber.text = "Dibuat oleh : ${data.getString("detailSumber")}"
                detailMarketing.text = "Marketing : ${data.getString("marketing")}"
                detailTanggal.text = "Dibuat tanggal : $valueTanggal0"
                detailDeadline.text = "Deadline : $valueDeadline"
                detailDesc.text = "Deskripsi : ${data.getString("detailDesc")}"
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

                noPK.text = "No PK : ${data.getString("noPK")}"

                anggotaNama.text = "Nama : ${data.getString("anggotaNama")}"
                anggotaAlamat.text = "Alamat : ${data.getString("anggotaAlamat")}"
                anggotaKontak.text = "Kontak : ${data.getString("anggotaKontak")}"

                val tanggal = DateTime(data.getString("angsuranTanggal"))
                val valueTanggal =
                    tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                angsuranTanggal.text = "Tanggal : $valueTanggal"
                angsuranNominal.text = "Nominal : ${data.getString("angsuranNominal")}"
                angsuranDenda.text = "Denda : ${data.getString("angsuranDenda")}"

                noPK.text = "No PK : ${data.getString("noPK")}"
                fasilitasDenda.text = "Total denda : ${data.getString("nominaldenda")}"
                fasilitasStatusDenda.text = "Status denda : ${data.getString("flagdenda")}"

                review.text = "Detil : ${data.getString("review")}"
                noteMarketing.text = "Detil : ${data.getString("noteMarketing")}"


                if (status.equals("draft", true)) {
//                    edit.visibility = View.VISIBLE
//                    delete.visibility = View.VISIBLE
//                    send.visibility = View.VISIBLE
//                    finish.visibility = View.INVISIBLE

//                    edit.isEnabled = View.VISIBLE
//                    delete.isEnabled = View.VISIBLE
//                    send.isEnabled = View.VISIBLE
                    finish.isEnabled = false
                }

                if (status.equals("to do", true)) {
//                    edit.visibility = View.INVISIBLE
//                    delete.visibility = View.INVISIBLE
//                    send.visibility = View.INVISIBLE
//                    finish.visibility = View.INVISIBLE

                    edit.isEnabled = false
                    delete.isEnabled = false
                    send.isEnabled = false
                    finish.isEnabled = false
                }

                if (status.equals("review", true)) {
//                    edit.visibility = View.INVISIBLE
//                    delete.visibility = View.INVISIBLE
//                    send.visibility = View.INVISIBLE
//                    finish.visibility = View.VISIBLE

                    edit.isEnabled = false
                    delete.isEnabled = false
                    send.isEnabled = false
                    finish.isEnabled = true
                }

                if (status.equals("Selesai", true) || status.equals("Ditolak", true)) {
//                    edit.visibility = View.INVISIBLE
//                    delete.visibility = View.INVISIBLE
//                    send.visibility = View.INVISIBLE
//                    finish.visibility = View.INVISIBLE


                    edit.isEnabled = false
                    delete.isEnabled = false
                    send.isEnabled = false
                    finish.isEnabled = false
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

    private fun backgroundDelete() {
        CoroutineScope(Dispatchers.IO).launch {

            val obj = IdVersionOnly(
                this@TaskManagerCollectionDetail.data.getString("idtask"),
                this@TaskManagerCollectionDetail.data.getInt("version")
            )

            onSuccessDelete(
                GentUtil().process(
                    this@TaskManagerCollectionDetail.resources.getString(R.string.deleteTaskManager),
                    obj,
                    this@TaskManagerCollectionDetail.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccessDelete(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                Toast.makeText(
                    requireContext(),
                    "Tugas Berhasil Dihapus",
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.findNavController().popBackStack()
            }
        }
    }

    private fun backgroundFinish(message: String, choice: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdMessageOnly(
                value.idtask, message
            )
            if (choice == 1) {
                onFinish(
                    GentUtil().process(
                        this@TaskManagerCollectionDetail.resources.getString(R.string.finishTaskManager),
                        obj,
                        this@TaskManagerCollectionDetail.requireContext()
                    )
                )

            }

            if (choice == 0) {
                onFinish(
                    GentUtil().process(
                        this@TaskManagerCollectionDetail.resources.getString(R.string.rejectTaskManager),
                        obj,
                        this@TaskManagerCollectionDetail.requireContext()
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
                    "Tugas Selesai",
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.findNavController().popBackStack()
            }
        }
    }

    private fun backgroundSend() {
        CoroutineScope(Dispatchers.IO).launch {
            val obj = IdVersionOnly(
                this@TaskManagerCollectionDetail.data.getString("idtask"),
                this@TaskManagerCollectionDetail.data.getInt("version")
            )

            onSuccessSend(
                GentUtil().process(
                    this@TaskManagerCollectionDetail.resources.getString(R.string.sendTaskManager),
                    obj,
                    this@TaskManagerCollectionDetail.requireContext()
                )
            )
        }
    }

    private suspend fun onSuccessSend(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                Toast.makeText(
                    requireContext(),
                    "Tugas Dikirim",
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