package com.example.kd.fragment.submission.loan.detail

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
import com.example.kd.fragment.submission.deposito.detail.Sub32DepositoDetailArgs
import com.example.kd.fragment.submission.deposito.detail.Sub32DepositoDetailDirections
import com.google.android.material.button.MaterialButton

class Sub31LoanDetail : Fragment() {

    companion object {
        fun newInstance() = Sub31LoanDetail()
    }

    private lateinit var viewModel: Sub31LoanDetailViewModel
    private lateinit var editButton: MaterialButton
    private val value: Sub31LoanDetailArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub31_loan_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Sub31LoanDetailViewModel::class.java)
        editButton = requireView().findViewById(R.id.edit)
        editButton.setOnClickListener {
            Toast.makeText(context,value.idDetailLoanSubmission, Toast.LENGTH_SHORT).show()
            val action = Sub31LoanDetailDirections.actionSub01LoanDetailToSub31LoanEdit(value.idDetailLoanSubmission)
            requireView().findNavController().navigate(action)
        }
        // TODO: Use the ViewModel
    }

}