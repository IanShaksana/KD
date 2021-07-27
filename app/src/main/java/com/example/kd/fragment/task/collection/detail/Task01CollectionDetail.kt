package com.example.kd.fragment.task.collection.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.kd.R
import com.example.kd.fragment.Test1Args

class Task01CollectionDetail : Fragment() {

    companion object {
        fun newInstance() = Task01CollectionDetail()
    }

    private lateinit var viewModel: Task01CollectionDetailViewModel
    private val value: Task01CollectionDetailArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_21_collection_02_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Task01CollectionDetailViewModel::class.java)
        val arg = value.value
        viewModel.initial(arg,activity)
        // TODO: Use the ViewModel
    }

}