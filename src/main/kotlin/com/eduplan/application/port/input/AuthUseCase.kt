package com.eduplan.application.port.input

import java.time.LocalDate
import java.util.UUID

interface AuthUseCase {
    fun register(command: RegisterCommand): RegisterResult

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
}