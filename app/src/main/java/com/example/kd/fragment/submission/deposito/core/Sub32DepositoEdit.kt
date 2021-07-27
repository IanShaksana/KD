package com.example.kd.fragment.submission.deposito.core

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kd.R
import com.example.kd.dialog.DialogDepositoProduk
import com.example.kd.dialog.DialogDepositoTipe
import com.google.android.material.textfield.TextInputEditText

class Sub32DepositoEdit : Fragment(),
    DialogDepositoProduk.dialogListener, DialogDepositoTipe.dialogListener {

    companion object {
        fun newInstance() = Sub32DepositoEdit()
    }

    private lateinit var viewModel: Sub32DepositoEditViewModel
    private lateinit var depositoProduk: TextInputEditText
    private lateinit var depositoTipe: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sub32_deposito_edit_fragment, container, false)
        depositoProduk = view.findViewById(R.id.depositoProduk)
        depositoTipe = view.findViewById(R.id.depositoTipe)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Sub32DepositoEditViewModel::class.java)
        depositoProduk.setOnClickListener {
            val dialog = DialogDepositoProduk()
            dialog.show(childFragmentManager, "Dialog Deposito Produk")
        }

        depositoTipe.setOnClickListener {
            val dialog = DialogDepositoTipe()
            dialog.show(childFragmentManager, "Dialog Deposito Tipe")
        }
    }

    override fun onDialogDepositoProdukClick(value: String) {
        depositoProduk.setText(value)
    }

    override fun onDialogDepositoTipeClick(value: String) {
        depositoTipe.setText(value)
    }

}