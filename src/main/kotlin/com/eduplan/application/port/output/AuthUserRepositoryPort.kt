package com.eduplan.application.port.output

import com.eduplan.domain.model.AuthUser

interface AuthUserRepositoryPort {
    fun save(user: AuthUser): AuthUser

    fun findByUsername(username: String): AuthUser?

    fun existsByUsername(username: String): Boolean
}