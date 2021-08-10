package com.example.kd.modelbody

import java.math.BigDecimal
import java.util.*

data class LoanCreate(

    val idmarketing: String,

    val title: String,
    val debiturNama: String,
    val debiturKontak: String,
    val debiturAlamat: String,
    val debiturNik: String,

    val loanProduk: String,
    val loanPlafon: String,
    val loanTime: String,
    val loanBungarate: String,
    val loanProvisirate: String,
    val loanTanggal: String,
    val loanTanggal1: String,
    val loanJenis: String,
    val loanTujuan: String,

    val jaminanTipe: String,
    val jaminanKepemilikan: String,
    val jaminanTahun: Integer,
    val jaminanNominal: String,
    val jaminanDeskripsi: String,

    val informasiTambahan: String

)
