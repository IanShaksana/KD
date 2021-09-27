package com.segen.kd.modelbody

import java.util.*

data class TaskModelManager(
    val id: String,
    val idmarketing: String,
    val marketing: String,
    val title: String,
    val tipe: String,
    val status: String,
    val deadline: Date,
    val attachment: String,
    val tanggal: String,
    val finishdate: String,
)
