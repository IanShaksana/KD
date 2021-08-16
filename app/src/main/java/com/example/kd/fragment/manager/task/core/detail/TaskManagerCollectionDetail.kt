package com.example.kd.fragment.manager.task.core.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kd.R
import com.example.kd.databinding.FragmentTaskManagerCollectionDetailBinding

class TaskManagerCollectionDetail : Fragment() {

    private lateinit var binding: FragmentTaskManagerCollectionDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskManagerCollectionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}