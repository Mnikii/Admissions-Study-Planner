package com.eduplan.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class UserDocument(
    override val id: UUID,
    val userId: UUID,
    var taskId: UUID?,
    var documentType: DocumentType,
    var fileName: String,
    var fileUrl: String,
    var fileSize: Long,
    var mimeType: String,
    var expiryDate: LocalDate?,
    var isVerified: Boolean = false,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    override var deletedAt: LocalDateTime? = null,
) : BaseEntity(id, createdAt, deletedAt) {

    override fun softDelete() {
        deletedAt = LocalDateTime.now()
    }

    fun isExpired(now: LocalDate = LocalDate.now()): Boolean = expiryDate?.isBefore(now) ?: false

    fun verify() {
        isVerified = true
        updatedAt = LocalDateTime.now()
    }
}
