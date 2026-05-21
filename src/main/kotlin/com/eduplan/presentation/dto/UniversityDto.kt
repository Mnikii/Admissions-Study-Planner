package com.eduplan.presentation.dto

import java.time.LocalDateTime
import java.util.UUID

data class UniversityResponseDto(
    val id: UUID,
    val createdAt: LocalDateTime,
    val deletedAt: LocalDateTime?,
    val name: String,
    val address: String,
    val country: String, 
    val website: String
    
)

data class CreateUniversityRequestDto(
    val name: String,
    val address: String,
    val country: String,
    val website: String
)

data class UpdateUniversityRequestDto(
    val name: String,
    val address: String,
    val country: String,
    val website: String
)
