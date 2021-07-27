package com.example.kd.fragment.submission.deposito.core

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kd.R

class Sub32DepositoCreate : Fragment() {

    companion object {
        fun newInstance() = Sub32DepositoCreate()
    }

    private lateinit var viewModel: Sub32DepositoCreateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub32_deposito_create_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Sub32DepositoCreateViewModel::class.java)
        // TODO: Use the ViewModel
    }

}