package com.example.kd.fragment.task.personal.detail

import androidx.lifecycle.ViewModelProvider
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
import com.example.kd.databinding.Frag22Collection02DetailBinding
import com.example.kd.modelbody.IdOnly
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

class Task02TaskDetail : Fragment() {


    private lateinit var viewModel: Task02TaskDetailViewModel
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
            backgroundFinish()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Task02TaskDetailViewModel::class.java)
        val arg = value.value
        viewModel.initial(arg, activity)
        // TODO: Use the ViewModel
    }


    private fun background() {
        CoroutineScope(Dispatchers.IO).launch {

            onSuccess(
                getTask(
                    this@Task02TaskDetail.requireContext().resources.getString(R.string.getTaskDetail),
                    JSONObject(
                        Gson().toJson(
                            IdOnly(
                                value.value
                            )
                        )
                    )
                )
            )
        }
    }

    private fun getTask(url: String, jObject: JSONObject): JSONObject {
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

                fileJumlah.text = "Jumlah File : ${data.getInt("fileJumlah")}"

                status.text = "Status : ${data.getString("status")}"

                review.text = "Detail : ${data.getString("review")}"

                finish.isEnabled = data.getString("status").equals("To Do",true)
                if (finish.isEnabled) {
                    finish.visibility = View.VISIBLE
                } else {
                    finish.visibility = View.INVISIBLE

                }
            }


        }
    }


    private fun backgroundFinish() {
        CoroutineScope(Dispatchers.IO).launch {

            onFinish(
                finish(
                    this@Task02TaskDetail.requireContext().resources.getString(R.string.finishTask),
                    JSONObject(
                        Gson().toJson(
                            IdOnly(
                                value.value
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



}