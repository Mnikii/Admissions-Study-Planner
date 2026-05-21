package com.eduplan.infrastructure.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
open class BaseAuditJpaEntity(
    @Column(name = "created_at", nullable = false)
    open val createdAt: LocalDateTime,
    @Column(name = "deleted_at")
    open var deletedAt: LocalDateTime? = null,
)
