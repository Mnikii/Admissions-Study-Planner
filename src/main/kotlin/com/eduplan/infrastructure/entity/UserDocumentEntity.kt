package com.eduplan.infrastructure.entity

import com.eduplan.domain.model.DocumentType
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_documents")
data class UserDocumentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "task_id")
    var taskId: UUID?,

    @Column(name = "document_type", nullable = false)
    @Enumerated(EnumType.STRING)
    var documentType: DocumentType,

    @Column(name = "file_name", nullable = false)
    var fileName: String,

    @Column(name = "file_url", nullable = false)
    var fileUrl: String,

    @Column(name = "file_size", nullable = false)
    var fileSize: Long,

    @Column(name = "mime_type", nullable = false)
    var mimeType: String,

    @Column(name = "expiry_date")
    var expiryDate: LocalDate?,

    @Column(name = "is_verified", nullable = false)
    var isVerified: Boolean = false,

    @Column(name = "created_at", nullable = false)
    override val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    override var deletedAt: LocalDateTime? = null,
) : com.eduplan.infrastructure.entity.BaseAuditJpaEntity(createdAt, deletedAt)
