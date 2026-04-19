package com.eduplan.application.service

import com.eduplan.application.port.input.UserUseCase
import com.eduplan.application.port.output.UserRepositoryPort
import com.eduplan.domain.model.StudentStatus
import com.eduplan.domain.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class UserService(
    private val userRepository: UserRepositoryPort,
) : UserUseCase {
    override fun createUser(command: UserUseCase.CreateUserCommand): User {
        val newUser =
            User(
                id = UUID.randomUUID(),
                username = command.username,
                firstName = command.firstName,
                lastName = command.lastName,
                email = command.email,
                phoneNumber = command.phoneNumber,
                birthday = command.birthday,
                status = StudentStatus.PENDING_VERIFICATION,
            )
        return userRepository.save(newUser)
    }

    override fun getUserById(id: UUID): User? = userRepository.findById(id)

    override fun updateUser(
        id: UUID,
        command: UserUseCase.UpdateUserCommand,
    ): User {
        val user = userRepository.findById(id) ?: throw Exception("User with ID $id not found")

        val updatedUser =
            user.copy(
                firstName = command.firstName,
                lastName = command.lastName,
                phoneNumber = command.phoneNumber,
            )

        return userRepository.save(updatedUser)
    }

    override fun deleteUser(id: UUID) {
        val user = userRepository.findById(id) ?: throw Exception("User with ID $id not found")

        if (user.isActive()) {
            val softDeletedUser =
                user.copy(
                    deletedAt = LocalDateTime.now(),
                )
            userRepository.save(softDeletedUser)
        }
    }

    override fun getAllUsers(): List<User> = userRepository.findAll().filter { it.isActive() }
}