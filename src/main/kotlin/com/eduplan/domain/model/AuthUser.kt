package com.eduplan.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class AuthUser(
    val id: UUID,
    val userId: UUID,
    val username: String,
    val passwordHash: String?,
    val role: UserRole,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
