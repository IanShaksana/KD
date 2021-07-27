package com.example.kd.fragment.submission.loan.core

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kd.R
import com.example.kd.dialog.DialogLoanProduk
import com.google.android.material.textfield.TextInputEditText

class Sub31LoanCreate : Fragment(),
    DialogLoanProduk.dialogListener {

    companion object {
        fun newInstance() = Sub31LoanCreate()
    }

    private lateinit var viewModel: Sub31LoanCreateViewModel
    private lateinit var loanProduk: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sub31_loan_create_fragment, container, false)
        loanProduk = view.findViewById(R.id.loanProduk)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Sub31LoanCreateViewModel::class.java)
        loanProduk.setOnClickListener {
            val dialog =DialogLoanProduk()
            dialog.show(childFragmentManager, "Dialog Loan Produk")
        }
        // TODO: Use the ViewModel
    }

    override fun onDialogClick(value: String) {
        loanProduk.setText(value)
    }

}