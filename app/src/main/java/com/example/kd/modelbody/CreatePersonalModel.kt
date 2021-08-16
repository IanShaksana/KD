package com.example.kd.modelbody

import java.util.*

data class CreatePersonalModel(

    val idmarketing: String,
    val title: String,
    val tipe: String,
    val tanggal: String,
    val description: String,
    val createdby: String,

    val detailSumber: String,
    val detailDeadline: String,
    val detailDesc: String,
)
