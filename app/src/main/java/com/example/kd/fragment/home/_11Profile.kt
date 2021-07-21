package com.example.kd.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.kd.R
import com.google.android.material.button.MaterialButton

class _11Profile : Fragment() {

    private lateinit var btn_left: MaterialButton
    private lateinit var btn_right: MaterialButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.frag_11_profile, container, false)
        btn_right = view.findViewById(R.id.right)
        btn_left = view.findViewById(R.id.left)

        btn_right.setOnClickListener {
            view.findNavController().navigate(R.id.action_nav_home_to_test1)

        }

        return view
    }

    private fun btn_right_push(v: View) {
        val amount: Float = 123.5F
    }

}