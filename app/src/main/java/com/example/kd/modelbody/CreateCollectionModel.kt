package com.example.kd.modelbody

import java.util.*

data class CreateCollectionModel(
    val idmarketing: String,
    val idpayment: String,
    val title: String,
    val tipe: String,
    val tanggal: Date,
    val description: String,
    val createdby: String
)
