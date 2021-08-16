package com.example.kd.fragment.manager.task.core.crud.collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kd.databinding.FragmentTaskManagerCollectionEditBinding

class TaskManagerCollectionEdit : Fragment() {

    private lateinit var binding: FragmentTaskManagerCollectionEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTaskManagerCollectionEditBinding.inflate(inflater, container, false)
        return binding.root
    }

}