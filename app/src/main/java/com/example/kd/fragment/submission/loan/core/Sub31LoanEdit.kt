package com.example.kd.fragment.submission.loan.core

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kd.R
import com.example.kd.dialog.DialogJaminanKepemelikan
import com.example.kd.dialog.DialogJaminanTipe
import com.example.kd.dialog.DialogLoanProduk
import com.google.android.material.textfield.TextInputEditText

class Sub31LoanEdit : Fragment(),
    DialogLoanProduk.dialogListener, DialogJaminanKepemelikan.dialogListener,
    DialogJaminanTipe.dialogListener {

    companion object {
        fun newInstance() = Sub31LoanEdit()
    }

    private lateinit var viewModel: Sub31LoanEditViewModel
    private lateinit var loanProduk: TextInputEditText
    private lateinit var jaminanTipe: TextInputEditText
    private lateinit var jaminanKepemilikan: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sub31_loan_edit_fragment, container, false)
        loanProduk = view.findViewById(R.id.loanProduk)
        jaminanTipe = view.findViewById(R.id.jaminanTipe)
        jaminanKepemilikan = view.findViewById(R.id.jaminanKepemilikan)


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Sub31LoanEditViewModel::class.java)
        loanProduk.setOnClickListener {
            val dialog = DialogLoanProduk()
            dialog.show(childFragmentManager, "Dialog Loan Produk")
        }

        jaminanTipe.setOnClickListener {
            val dialog = DialogJaminanTipe()
            dialog.show(childFragmentManager, "Dialog Jaminan Tipe")
        }

        jaminanKepemilikan.setOnClickListener {
            val dialog = DialogJaminanKepemelikan()
            dialog.show(childFragmentManager, "Dialog Jaminan Kepemilikan")
        }
    }

    override fun onDialogClick(value: String) {
        loanProduk.setText(value)
    }

    override fun onDialogJaminanKepemilikanClick(value: String) {
        jaminanKepemilikan.setText(value)
    }

    override fun onDialogJaminanTipeClick(value: String) {
        jaminanTipe.setText(value)
    }

}