package com.eduplan.domain.model

import java.time.LocalDateTime

open class BaseAuditEntity(
    open val createdAt: LocalDateTime = LocalDateTime.now(),
    open var deletedAt: LocalDateTime? = null,
) {
    open fun isActive(): Boolean = deletedAt == null

    open fun softDelete() {
        deletedAt = LocalDateTime.now()
    }

    fun restore() {
        deletedAt = null
    }
}
