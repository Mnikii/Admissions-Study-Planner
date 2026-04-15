package com.eduplan.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class University(
    val id: UUID,
    var name: String,
    val address: String,
    val country: String,
    val website: String,
    var createdAt: LocalDateTime,
    var deletedAt: LocalDateTime?,
    //val structure: MutableMap<String, MutableList<String>> // Key - faculty; Value - list of programmes on a faculty
) {
}