package com.segen.kd.modelbody

data class DepositoEdit(

    val id: String,
    val idmarketing: String?,

    val pengajuanTitle: String,
    val deposanNama: String,
    val deposanKontak: String,
    val deposanAlamat: String,
    val deposanNik: String,

    val pengajuanProduk: String,
    val pengajuanTipe: String,
    val pengajuanSaldoAwal: String,
    val pengajuanBungaRate: String,
    val pengajuanTanggal: String,

    val informasiTambahan: String,


    val version: Int
)
