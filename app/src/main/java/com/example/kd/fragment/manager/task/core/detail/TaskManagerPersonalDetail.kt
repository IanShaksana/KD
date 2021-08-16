package com.example.kd.fragment.manager.task.core.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.kd.R
import com.example.kd.databinding.FragmentTaskManagerPersonalDetailBinding
import com.example.kd.dialog.DialogInputMarketing
import com.example.kd.fragment.manager.task.core.crud.personal.TaskManagerPersonalEditArgs
import com.example.kd.fragment.marketing.task.personal.detail.Task02TaskDetailDirections
import com.example.kd.modelbody.IdMessageOnly
import com.example.kd.modelbody.IdOnly
import com.example.kd.modelbody.ListMarketingModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class TaskManagerPersonalDetail : Fragment(), DialogInputMarketing.dialogListener {

    private lateinit var binding: FragmentTaskManagerPersonalDetailBinding
    private val value: TaskManagerPersonalDetailArgs by navArgs()
    private lateinit var data: JSONObject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskManagerPersonalDetailBinding.inflate(inflater, container, false)
        backgroundInitial()
        binding.edit.setOnClickListener {
            val action =
                TaskManagerPersonalDetailDirections.actionTaskManagerPersonalDetailToTaskManagerPersonalEdit(
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
            val dialog = DialogInputMarketing()
            dialog.show(childFragmentManager, "Dialog Input Marketing")
        }
        return binding.root
    }

    private fun backgroundInitial() {
        CoroutineScope(Dispatchers.IO).launch {
            onSuccess(
                getDetail(
                    this@TaskManagerPersonalDetail.requireContext().resources.getString(R.string.getTaskDetailManager),
                    JSONObject(
                        Gson().toJson(
                            IdOnly(
                                value.idtask
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
            this@TaskManagerPersonalDetail.data = data
            binding.apply {
                detailSumber.text = "Sumber : ${data.getString("detailSumber")}"

                val tanggal = DateTime(data.getString("detailTanggal"))
                val deadline = DateTime(data.getString("detailDeadline"))
                val valueTanggal =
                    tanggal.toString(activity?.resources?.getString(R.string.format_date_1))
                val valueDeadline =
                    deadline.toString(activity?.resources?.getString(R.string.format_date_2))

                detailTanggal.text = "Tanggal : $valueTanggal"
                detailDeadline.text = "Deadline : $valueDeadline"
                detailDesc.text = "Deskripsi : ${data.getString("detailDesc")}"
                detailMarketing.text = "Marketing : ${data.getString("marketing")}"

                fileJumlah.text = "Jumlah File : ${data.getInt("fileJumlah")}"

                status.text = "Status : ${data.getString("status")}"

                review.text = "Detail : ${data.getString("review")}"
                noteMarketing.text = "Detail : ${data.getString("noteMarketing")}"

//                finish.isEnabled = data.getString("status").equals("To Do", true)
//                if (finish.isEnabled) {
//                    finish.visibility = View.VISIBLE
//                } else {
//                    finish.visibility = View.INVISIBLE
//
//                }

                val status: String = data.getString("status")
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

            }

        }


    }

    private fun backgroundDelete() {
        CoroutineScope(Dispatchers.IO).launch {

            onSuccessDelete(
                delete(
                    this@TaskManagerPersonalDetail.requireContext().resources.getString(R.string.deleteTaskManager),
                    this@TaskManagerPersonalDetail.data
                )
            )
        }
    }

    private fun delete(url: String, jObject: JSONObject): JSONObject {
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

    private suspend fun onSuccessDelete(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                binding.root.findNavController().popBackStack()
            }
        }
    }

    private fun backgroundFinish(message: String) {
        CoroutineScope(Dispatchers.IO).launch {

            onFinish(
                finish(
                    this@TaskManagerPersonalDetail.requireContext().resources.getString(R.string.finishTaskManager),
                    JSONObject(
                        Gson().toJson(
                            IdMessageOnly(
                                value.idtask, message
                            )
                        )
                    )
                )
            )
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
                binding.root.findNavController().popBackStack()
            }
        }
    }

    private fun backgroundSend() {
        CoroutineScope(Dispatchers.IO).launch {

            onSuccessSend(
                send(
                    this@TaskManagerPersonalDetail.requireContext().resources.getString(R.string.sendTaskManager),
                    this@TaskManagerPersonalDetail.data
                )
            )
        }
    }

    private fun send(url: String, jObject: JSONObject): JSONObject {
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

    private suspend fun onSuccessSend(resp: JSONObject) {
        withContext(Dispatchers.Main) {
            if (resp["status"] == 1) {
                binding.root.findNavController().popBackStack()
            }
        }
    }

    override fun onDialogInputMarketingClick(value: String) {
        backgroundFinish(value)
    }

}