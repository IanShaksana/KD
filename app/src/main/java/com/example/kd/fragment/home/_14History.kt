package com.example.kd.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kd.R
import com.google.android.material.button.MaterialButton

class _14History : Fragment() {

    private var btn_left: MaterialButton? = null
    private var btn_right: MaterialButton? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.frag_14_history, container, false)
        btn_right = view.findViewById(R.id.right)
        btn_left = view.findViewById(R.id.left)

       return  view
    }

}