package com.eduplan.infrastructure.service

import com.eduplan.application.port.output.AuthUserRepositoryPort
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class DatabaseUserDetailsService(
    private val authUserRepository: AuthUserRepositoryPort,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val authUser = authUserRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        return User.builder()
            .username(authUser.username)
            .password(authUser.passwordHash)
            .roles(authUser.role.name)
            .build()
    }
}