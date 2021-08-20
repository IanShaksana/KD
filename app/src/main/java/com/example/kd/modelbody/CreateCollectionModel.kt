package com.example.kd.modelbody


data class CreateCollectionModel(
    val idmarketing: String,
    val idcollection: String,
    val title: String,
    val tipe: String,
    val tanggal: String,
    val description: String,
    val createdby: String
)
