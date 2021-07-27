package com.example.kd.fragment.submission.loan.core

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kd.R

class Sub31LoanEdit : Fragment() {

    companion object {
        fun newInstance() = Sub31LoanEdit()
    }

    private lateinit var viewModel: Sub31LoanEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub31_loan_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Sub31LoanEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}