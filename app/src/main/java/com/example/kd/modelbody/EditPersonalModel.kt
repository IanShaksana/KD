package com.example.kd.modelbody

data class EditPersonalModel(

    val id: String,
    val idmarketing: String,
    val title: String,
    val tipe: String,
    val tanggal: String,
    val description: String,
    val lastmodifiedby: String,

    val detailSumber: String,
    val detailDeadline: String,
    val detailDesc: String,

    val version: Int
)