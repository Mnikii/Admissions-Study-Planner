package com.eduplan.infrastructure.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "universities")
data class UniversityJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,
    @Column(name = "name", nullable = false, unique = true)
    val name: String,
    @Column(name = "address", nullable = false)
    val address: String,
    @Column(name = "country", nullable = false)
    val country: String,
    @Column(name = "website", nullable = false)
    val website: String,
    @Column(nullable = false)
    override var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column
    override var deletedAt: LocalDateTime? = null,
) : BaseAuditJpaEntity(createdAt, deletedAt)
