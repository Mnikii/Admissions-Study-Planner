package com.eduplan.infrastructure.mapper

import com.eduplan.domain.model.UserDocument
import com.eduplan.infrastructure.entity.UserDocumentEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserDocumentMapper {
    fun toDomain(e: UserDocumentEntity): UserDocument = UserDocument(
        id = e.id,
        userId = e.userId,
        taskId = e.taskId,
        documentType = e.documentType,
        fileName = e.fileName,
        fileUrl = e.fileUrl,
        fileSize = e.fileSize,
        mimeType = e.mimeType,
        expiryDate = e.expiryDate,
        isVerified = e.isVerified,
        createdAt = e.createdAt,
        updatedAt = e.updatedAt,
        deletedAt = e.deletedAt,
    )

    fun toJpa(d: UserDocument): UserDocumentEntity = UserDocumentEntity(
        id = d.id,
        userId = d.userId,
        taskId = d.taskId,
        documentType = d.documentType,
        fileName = d.fileName,
        fileUrl = d.fileUrl,
        fileSize = d.fileSize,
        mimeType = d.mimeType,
        expiryDate = d.expiryDate,
        isVerified = d.isVerified,
        createdAt = d.createdAt,
        updatedAt = d.updatedAt,
        deletedAt = d.deletedAt,
    )
}
