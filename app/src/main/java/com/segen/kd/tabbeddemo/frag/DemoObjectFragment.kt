package com.segen.kd.tabbeddemo.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.segen.kd.databinding.FragmentCollectionObjectBinding

private const val ARG_OBJECT = "object"

class DemoObjectFragment : Fragment() {


    private lateinit var binding: FragmentCollectionObjectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCollectionObjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            binding.text1.text = getInt(ARG_OBJECT).toString();
        }
    }
}