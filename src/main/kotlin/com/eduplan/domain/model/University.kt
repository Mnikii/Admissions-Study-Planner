package com.eduplan.domain.model

import java.util.UUID

data class University(
    val id: UUID,
    var name: String,
    val address: String,
    val website: String,
//    val structure: MutableMap<String, MutableList<String>> // Key - faculty; Value - list of programmes on a faculty
) {
}