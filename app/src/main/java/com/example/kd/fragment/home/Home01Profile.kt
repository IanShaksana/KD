package com.example.kd.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.kd.R
import com.google.android.material.button.MaterialButton

class Home01Profile : Fragment() {

    private lateinit var btnLeft: MaterialButton
    private lateinit var btnRight: MaterialButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.frag_11_profile, container, false)

        return view
    }

    private fun btnRightPush1(v: View) {
        val action = Home01ProfileDirections.actionNavHomeToTest1(123)
        v.findNavController().navigate(action)
    }



}