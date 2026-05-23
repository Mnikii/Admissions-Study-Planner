package com.eduplan.application.port.input

import java.time.LocalDate
import java.util.UUID

interface AuthUseCase {
    fun register(command: RegisterCommand): RegisterResult
    fun login(command: LoginCommand): LoginResult
    fun me(username: String): CurrentUserResult

    data class RegisterCommand(
        val username: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val phoneNumber: String,
        val birthday: LocalDate,
    )

    data class RegisterResult(
        val userId: UUID,
        val username: String,
        val role: String,
    )

    data class LoginCommand(
        val username: String,
        val password: String,
    )

    data class LoginResult(
        val userId: UUID,
        val username: String,
        val role: String,
        val accessToken: String,
        val tokenType: String,
        val expiresAtEpochMillis: Long,
    )

    data class CurrentUserResult(
        val userId: UUID,
        val username: String,
        val role: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val phoneNumber: String,
        val birthday: LocalDate,
    )
}