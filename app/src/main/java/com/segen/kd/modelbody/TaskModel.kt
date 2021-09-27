package com.segen.kd.modelbody

import java.util.*

data class TaskModel(
    val id: String,
    val title: String,
    val tipe: String,
    val status: String,
    val deadline: Date,
    val attachment: String,
    val tanggal: String,
    val finishdate: String,
)
