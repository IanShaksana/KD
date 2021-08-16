package com.example.kd.fragment.marketing.task.collection.detail

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel

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