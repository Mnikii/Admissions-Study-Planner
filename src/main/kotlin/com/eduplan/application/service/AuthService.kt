package com.eduplan.application.service

import com.eduplan.application.port.input.AuthUseCase
import com.eduplan.application.port.output.AuthUserRepositoryPort
import com.eduplan.application.port.output.UserRepositoryPort
import com.eduplan.common.util.JwtService
import com.eduplan.domain.model.AuthUser
import com.eduplan.domain.model.UserRole
import org.springframework.security.authentication.BadCredentialsException
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
    private val jwtService: JwtService,
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

    override fun login(command: AuthUseCase.LoginCommand): AuthUseCase.LoginResult {
        val authUser =
            authUserRepository.findByUsername(command.username)
                ?: throw BadCredentialsException("Invalid username or password")

        val passwordHash = authUser.passwordHash ?: throw BadCredentialsException("Invalid username or password")
        if (!passwordEncoder.matches(command.password, passwordHash)) {
            throw BadCredentialsException("Invalid username or password")
        }

        val userDetails =
            org.springframework.security.core.userdetails.User.builder()
                .username(authUser.username)
                .password(authUser.passwordHash)
                .roles(authUser.role.name)
                .build()

        val accessToken = jwtService.generateToken(userDetails)

        return AuthUseCase.LoginResult(
            userId = authUser.userId,
            username = authUser.username,
            role = authUser.role.name,
            accessToken = accessToken,
            tokenType = "Bearer",
            expiresAtEpochMillis = jwtService.extractExpirationDate(accessToken).time,
        )
    }

    override fun me(username: String): AuthUseCase.CurrentUserResult {
        val user = userRepository.findByUsername(username) ?: throw IllegalArgumentException("User not found")
        val authUser = authUserRepository.findByUsername(username) ?: throw IllegalArgumentException("Auth user not found")

        return AuthUseCase.CurrentUserResult(
            userId = user.id,
            username = user.username,
            role = authUser.role.name,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            phoneNumber = user.phoneNumber,
            birthday = user.birthday,
        )
    }
}