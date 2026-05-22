package com.eduplan.application.service

import com.eduplan.application.port.output.FileStoragePort
import com.eduplan.application.port.output.UserDocumentOutputPort
import com.eduplan.domain.model.DocumentType
import com.eduplan.domain.model.UserDocument
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class UserDocumentServiceTest {
    private val repo = mockk<UserDocumentOutputPort>(relaxed = true)
    private val storage = mockk<FileStoragePort>(relaxed = true)
    private val encryption = mockk<com.eduplan.application.port.output.EncryptionPort>(relaxed = true)
    private val service = UserDocumentApplicationService(repo, storage, encryption)

    @Test
    fun `upload stores and saves metadata`() {
        val userId = UUID.randomUUID()
        val content = ByteArrayInputStream("hello".toByteArray())
        every { storage.store(any(), any(), any(), any()) } returns "s3://bucket/key"
        every { repo.save(any()) } answers { firstArg() as UserDocument }

        val res = service.upload(userId, "a.pdf", "application/pdf", 5, content, DocumentType.PASSPORT, null, null)
        assertEquals(userId, res.userId)
        assertEquals(DocumentType.PASSPORT, res.documentType)
    }
}
