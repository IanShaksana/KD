package com.example.kd.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.kd.R
import com.example.kd.fragment.task.collection.Task01CollectionDirections
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class Home01Profile : Fragment() {

    private lateinit var task: MaterialCardView
    private lateinit var att: MaterialCardView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.frag_11_profile, container, false)

        task = view.findViewById(R.id.task_card)
        task.setOnClickListener {
            val action =
                Home01ProfileDirections.actionNavHomeToNavCollection()
            view.findNavController().navigate(action)
        }

        att = view.findViewById(R.id.att_card)
        att.setOnClickListener {
            val action =
                Home01ProfileDirections.actionNavHomeToNavAttendance()
            view.findNavController().navigate(action)

        }

        return view
    }


}