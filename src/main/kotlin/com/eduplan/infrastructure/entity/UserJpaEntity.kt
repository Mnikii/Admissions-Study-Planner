package com.eduplan.infrastructure.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import com.eduplan.domain.model.StudentStatus

@Entity
@Table(name = "users")
data class UserJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,
    @Column(name = "username", nullable = false, unique = true)
    val username: String,
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false)
    val firstName: String,
    @Column(nullable = false)
    val lastName: String,
    @Column(nullable = false)
    val phoneNumber: String,
    @Column(nullable = false)
    val birthday: LocalDate,
    @Column(nullable = false)
    var status: StudentStatus,
    @Column(nullable = false)
    override var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column
    override var deletedAt: LocalDateTime? = null,
) : BaseAuditJpaEntity(createdAt, deletedAt)
