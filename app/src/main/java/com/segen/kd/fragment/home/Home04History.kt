package com.segen.kd.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.segen.kd.R
import com.google.android.material.button.MaterialButton

class Home04History : Fragment() {

    private var btnLeft: MaterialButton? = null
    private var btnRight: MaterialButton? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.frag_14_history, container, false)
        btnRight = view.findViewById(R.id.right)
        btnLeft = view.findViewById(R.id.left)

       return  view
    }

}