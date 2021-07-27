package com.example.kd.fragment.submission.deposito.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kd.R
import com.example.kd.fragment.task.collection.detail.Task01CollectionDetailArgs
import com.google.android.material.button.MaterialButton

class Sub32DepositoDetail : Fragment() {

    companion object {
        fun newInstance() = Sub32DepositoDetail()
    }

    private lateinit var viewModel: Sub32DepositoDetailViewModel
    private lateinit var editButton: MaterialButton
    private val value: Sub32DepositoDetailArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub32_deposito_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Sub32DepositoDetailViewModel::class.java)
        editButton = requireView().findViewById(R.id.edit)
        editButton.setOnClickListener {
            Toast.makeText(context,value.idDetailDepositoSubmisson,Toast.LENGTH_SHORT).show()
            val action = Sub32DepositoDetailDirections.actionSub02DepositoDetailToSub32DepositoEdit(value.idDetailDepositoSubmisson)
            requireView().findNavController().navigate(action)
        }
        // TODO: Use the ViewModel
    }

}