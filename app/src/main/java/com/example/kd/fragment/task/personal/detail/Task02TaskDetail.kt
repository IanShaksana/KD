package com.example.kd.fragment.task.personal.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.kd.R

class Task02TaskDetail : Fragment() {

    companion object {
        fun newInstance() = Task02TaskDetail()
    }

    private lateinit var viewModel: Task02TaskDetailViewModel
    private val value: Task02TaskDetailArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_22_collection_02_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Task02TaskDetailViewModel::class.java)
        val arg = value.value
        viewModel.initial(arg,activity)
        // TODO: Use the ViewModel
    }

}