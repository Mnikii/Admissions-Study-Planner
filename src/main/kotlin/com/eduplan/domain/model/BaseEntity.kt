package com.eduplan.domain.model

import java.time.LocalDateTime
import java.util.*
import com.eduplan.common.util.IdGenerator

open class BaseEntity(
    open val id: UUID = IdGenerator.generateUUID(),
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override var deletedAt: LocalDateTime? = null,
) : BaseAuditEntity(createdAt, deletedAt)
