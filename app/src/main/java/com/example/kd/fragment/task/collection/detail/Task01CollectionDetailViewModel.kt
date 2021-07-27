package com.example.kd.fragment.task.collection.detail

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs

class Task01CollectionDetailViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val nama: String = ""
    private val nopk: String = ""

    private val tanggalAngsuran: String = ""
    private val urutanAngsuran: String = ""
    private val nominalAngsuran: String = ""
    private val dendaAngsuran: String = ""

    public fun initial(value: String, con : FragmentActivity?) {
        Toast.makeText(con,"arg "+value,Toast.LENGTH_SHORT).show()
    }


}