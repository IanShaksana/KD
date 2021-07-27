package com.example.kd.fragment.submission.loan.core

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kd.R

class Sub31LoanCreate : Fragment() {

    companion object {
        fun newInstance() = Sub31LoanCreate()
    }

    private lateinit var viewModel: Sub31LoanCreateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub31_loan_create_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Sub31LoanCreateViewModel::class.java)
        // TODO: Use the ViewModel
    }

}