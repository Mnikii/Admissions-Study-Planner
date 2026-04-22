package com.eduplan.presentation.dto

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class UserResponseDto(
    val id: UUID,
    val createdAt: LocalDateTime,
    val deletedAt: LocalDateTime?,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val birthday: LocalDate,
)

data class CreateUserRequestDto(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val birthday: LocalDate,
)

data class UpdateUserRequestDto(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
)
