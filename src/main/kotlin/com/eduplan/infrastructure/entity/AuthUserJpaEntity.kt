package com.eduplan.infrastructure.entity

import com.eduplan.domain.model.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.UUID

@Entity
@Table(
    name = "auth_users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["username"]),
        UniqueConstraint(columnNames = ["user_id"]),
    ],
)
data class AuthUserJpaEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column(name = "user_id", nullable = false, unique = true)
    val userId: UUID = UUID.randomUUID(),
    @Column(nullable = false, unique = true)
    val username: String = "",
    @Column(name = "password_hash", nullable = false)
    val passwordHash: String? = null,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole = UserRole.USER,
)
