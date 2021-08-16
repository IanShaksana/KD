package com.example.kd.modelbody

import java.util.*

data class CreateTaskBaseModel(
    val idmarketing: String,
    val title: String,
    val tipe: String,
    val tanggal: Date,
    val description: String,
    val createdby: String
)
