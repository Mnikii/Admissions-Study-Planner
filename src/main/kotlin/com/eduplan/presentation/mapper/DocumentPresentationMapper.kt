package com.eduplan.presentation.mapper

import com.eduplan.domain.model.UserDocument
import com.eduplan.presentation.dto.DocumentResponse
import org.springframework.stereotype.Component

@Component
class DocumentPresentationMapper {
    fun toResponse(d: UserDocument): DocumentResponse = DocumentResponse(
        id = d.id,
        userId = d.userId,
        taskId = d.taskId,
        documentType = d.documentType,
        fileName = d.fileName,
        fileSize = d.fileSize,
        mimeType = d.mimeType,
        fileUrl = d.fileUrl,
        expiryDate = d.expiryDate,
        isVerified = d.isVerified,
        uploadedAt = d.createdAt,
        updatedAt = d.updatedAt,
    )
}
