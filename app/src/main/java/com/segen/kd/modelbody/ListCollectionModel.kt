package com.segen.kd.modelbody

data class ListCollectionModel(
    val id: String,
    val nopk: String,
    val nama: String,
    val kontak: String,
    val alamat: String,
    val angsuranTanggal: String,
    val angsuranNominal: String,
    val angsuranDenda: String,
    val flagdenda: String,
    val nominaldenda: String,
)
