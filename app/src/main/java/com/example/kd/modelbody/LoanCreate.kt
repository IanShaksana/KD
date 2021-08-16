package com.example.kd.modelbody


data class LoanCreate(

    val idmarketing: String?,

    val pengajuanTitle: String,
    val debiturNama: String,
    val debiturKontak: String,
    val debiturAlamat: String,
    val debiturNik: String,

    val pengajuanProduk: String,
    val pengajuanPlafon: String,
    val pengajuanJangkaWaktu: String,
    val pengajuanBungaRate: String,
    val pengajuanProvisiRate: String,
    val pengajuanTanggal: String,
    val pengajuanTanggalAngsuranPertama: String,
    val pengajuanJenisPenggunaan: String,
    val pengajuanTujuanKredit: String,

    val jaminanTipe: String,
    val jaminanKepemilikan: String,
    val jaminanTahun: String,
    val jaminanNominal: String,
    val jaminanDeskripsi: String,

    val informasiTambahan: String

)
