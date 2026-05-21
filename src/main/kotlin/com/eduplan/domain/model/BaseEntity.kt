package com.eduplan.domain.model

import java.time.LocalDateTime
import java.util.*

open class BaseEntity(
//    open val id: UUID = IdGenerator.generateUUID(),
    open val id: UUID,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override var deletedAt: LocalDateTime? = null,
) : BaseAuditEntity(createdAt, deletedAt)
