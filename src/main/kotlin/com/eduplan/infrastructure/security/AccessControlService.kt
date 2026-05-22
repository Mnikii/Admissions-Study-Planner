package com.eduplan.infrastructure.security

import com.eduplan.application.port.output.UserRepositoryPort
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AccessControlService(
    private val userRepository: UserRepositoryPort,
) {
    fun canAccessUser(userId: UUID, authentication: Authentication?): Boolean {
        if (authentication == null) {
            return false
        }

        val isAdmin = authentication.authorities.any { it.authority == "ROLE_ADMIN" }
        if (isAdmin) {
            return true
        }

        val user = userRepository.findById(userId) ?: return false
        return user.username == authentication.name
    }
}
