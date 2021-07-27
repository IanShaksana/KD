package com.example.kd.fragment.submission.deposito.core

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kd.R

class Sub32DepositoEdit : Fragment() {

    companion object {
        fun newInstance() = Sub32DepositoEdit()
    }

    private lateinit var viewModel: Sub32DepositoEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub32_deposito_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Sub32DepositoEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}