package com.eduplan.application.service

import com.eduplan.application.port.input.AuthUseCase
import com.eduplan.application.port.output.AuthUserRepositoryPort
import com.eduplan.application.port.output.UserRepositoryPort
import com.eduplan.domain.model.AuthUser
import com.eduplan.domain.model.UserRole
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class AuthService(
    private val authUserRepository: AuthUserRepositoryPort,
    private val userRepository: UserRepositoryPort,
    private val userUseCase: com.eduplan.application.port.input.UserUseCase,
    private val passwordEncoder: PasswordEncoder,
) : AuthUseCase {
    override fun register(command: AuthUseCase.RegisterCommand): AuthUseCase.RegisterResult {
        if (authUserRepository.existsByUsername(command.username)) {
            throw IllegalArgumentException("Username already exists")
        }

        if (userRepository.findByUsername(command.username) != null) {
            throw IllegalArgumentException("User profile already exists")
        }

        val createdUser =
            userUseCase.createUser(
                com.eduplan.application.port.input.UserUseCase.CreateUserCommand(
                    username = command.username,
                    firstName = command.firstName,
                    lastName = command.lastName,
                    email = command.email,
                    phoneNumber = command.phoneNumber,
                    birthday = command.birthday,
                ),
            )

        val authUser =
            AuthUser(
                id = UUID.randomUUID(),
                userId = createdUser.id,
                username = command.username,
                passwordHash = passwordEncoder.encode(command.password),
                role = UserRole.USER,
            )

        val savedAuthUser = authUserRepository.save(authUser)

        return AuthUseCase.RegisterResult(
            userId = createdUser.id,
            username = savedAuthUser.username,
            role = savedAuthUser.role.name,
        )
    }
}