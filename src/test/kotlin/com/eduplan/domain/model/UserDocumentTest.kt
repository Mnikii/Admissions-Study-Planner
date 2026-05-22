package com.eduplan.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class UserDocumentTest {
    @Test
    fun `isExpired returns true when expiry date before today`() {
        val doc = UserDocument(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            taskId = null,
            documentType = DocumentType.OTHER,
            fileName = "a.pdf",
            fileUrl = "url",
            fileSize = 10,
            mimeType = "application/pdf",
            expiryDate = LocalDate.now().minusDays(1),
            isVerified = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        assertTrue(doc.isExpired())
    }

    @Test
    fun `verify sets isVerified true`() {
        val doc = UserDocument(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            taskId = null,
            documentType = DocumentType.OTHER,
            fileName = "a.pdf",
            fileUrl = "url",
            fileSize = 10,
            mimeType = "application/pdf",
            expiryDate = null,
            isVerified = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        doc.verify()
        assertTrue(doc.isVerified)
    }
}
