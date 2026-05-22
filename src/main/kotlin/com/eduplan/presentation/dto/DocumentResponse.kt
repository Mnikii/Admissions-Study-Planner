package com.eduplan.presentation.dto

import com.eduplan.domain.model.DocumentType
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class DocumentResponse(
    val id: UUID,
    val userId: UUID,
    val taskId: UUID?,
    val documentType: DocumentType,
    val fileName: String,
    val fileSize: Long,
    val mimeType: String,
    val fileUrl: String,
    val expiryDate: LocalDate?,
    val isVerified: Boolean,
    val uploadedAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
