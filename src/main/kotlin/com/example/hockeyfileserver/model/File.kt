package com.example.hockeyfileserver.model

import java.util.*

data class File(
    val id: UUID,
    val name: String,
    val type: String,
    val data: ByteArray,
)