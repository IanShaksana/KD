package com.example.kd.fragment.manager.task.crud

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.kd.databinding.FragmentTaskManagerCrudSelectBinding

class TaskManagerCrudSelect : Fragment() {

    private lateinit var binding: FragmentTaskManagerCrudSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskManagerCrudSelectBinding.inflate(inflater, container, false)
        binding.createCollection.setOnClickListener {
            val action = TaskManagerCrudSelectDirections.actionTaskManagerCrudSelectToTaskManagerCollectionCreate()
            binding.root.findNavController().navigate(action)
        }

        binding.createPersonal.setOnClickListener {
            val action = TaskManagerCrudSelectDirections.actionTaskManagerCrudSelectToTaskManagerPersonalCreate()
            binding.root.findNavController().navigate(action)
        }
        return binding.root
    }

}