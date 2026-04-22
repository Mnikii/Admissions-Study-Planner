package com.eduplan.application.port.input

import com.eduplan.domain.model.User
import java.time.LocalDate
import java.util.*

interface UserUseCase {
    fun createUser(command: CreateUserCommand): User

    fun getUserById(id: UUID): User?

    fun updateUser(
        id: UUID,
        command: UpdateUserCommand,
    ): User

    fun deleteUser(id: UUID)

    fun getAllUsers(): List<User>


    data class CreateUserCommand(
        val username: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val phoneNumber: String,
        val birthday: LocalDate,
    )

    data class UpdateUserCommand(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
    )
}