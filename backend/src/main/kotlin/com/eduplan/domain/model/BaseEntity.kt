package com.eduplan.domain.model

import java.time.LocalDateTime
import java.util.*

open class BaseEntity(
    open val id: UUID = UUID.randomUUID(), // ID generation has not yet been implemented
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override var deletedAt: LocalDateTime? = null,
) : BaseAuditEntity(createdAt, deletedAt)

