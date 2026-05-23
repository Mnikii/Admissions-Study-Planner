package com.eduplan.presentation.dto

import java.time.LocalDate
import java.util.UUID

data class RegisterRequestDto(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val birthday: LocalDate,
)

data class RegisterResponseDto(
    val userId: UUID,
    val username: String,
    val role: String,
)

data class LoginRequestDto(
    val username: String,
    val password: String,
)

data class LoginResponseDto(
    val userId: UUID,
    val username: String,
    val role: String,
    val accessToken: String,
    val tokenType: String,
    val expiresAtEpochMillis: Long,
)

data class MeResponseDto(
    val userId: UUID,
    val username: String,
    val role: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val birthday: LocalDate,
)

data class ErrorResponseDto(
    val error: String,
)