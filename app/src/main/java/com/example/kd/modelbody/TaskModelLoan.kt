package com.example.kd.modelbody

import java.util.*

data class TaskModelLoan(
    val id: String,
    val pengajuanTitle: String,
    val pengajuanProduk: String,
    val pengajuanTipe: String,
    val status: String,
    val deadline: Date,
    val attachment: String,
    val tanggal: String
)
