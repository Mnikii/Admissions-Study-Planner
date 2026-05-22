package com.eduplan.application.service

import com.eduplan.application.port.output.EncryptionPort
import com.eduplan.application.port.output.FileStoragePort
import com.eduplan.application.port.output.UserDocumentOutputPort
import com.eduplan.domain.model.UserDocument
import com.eduplan.domain.model.DocumentType
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
class UserDocumentApplicationService(
    private val repository: UserDocumentOutputPort,
    private val storage: FileStoragePort,
    private val encryption: EncryptionPort,
) {

    fun upload(userId: UUID, fileName: String, contentType: String, size: Long, content: InputStream, documentType: DocumentType, taskId: UUID?, expiryDate: LocalDate?): UserDocument {
        if (size > 10L * 1024L * 1024L) throw IllegalArgumentException("File size exceeds 10MB")
        val ext = fileName.substringAfterLast('.', fileName)
        val uuid = UUID.randomUUID()
        val path = "uploads/$userId/$uuid.$ext"
        val storedUrl = storage.store(path, content, size, contentType)

        val doc = UserDocument(
            id = UUID.randomUUID(),
            userId = userId,
            taskId = taskId,
            documentType = documentType,
            fileName = fileName,
            fileUrl = storedUrl,
            fileSize = size,
            mimeType = contentType,
            expiryDate = expiryDate,
            isVerified = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )

        return repository.save(doc)
    }

    fun getAllForUser(requesterId: UUID, userId: UUID, type: DocumentType?): List<UserDocument> {
        if (requesterId != userId) throw IllegalAccessException("Can only view own documents")
        val all = repository.findAllByUserId(userId)
        return if (type == null) all else all.filter { it.documentType == type }
    }

    fun getById(requesterId: UUID, id: UUID): UserDocument? {
        val doc = repository.findById(id) ?: return null
        if (doc.userId != requesterId) throw IllegalAccessException("Access denied")
        return doc
    }

    fun download(requesterId: UUID, id: UUID): Resource? {
        val doc = getById(requesterId, id) ?: return null
        return storage.getResource(doc.fileUrl)
    }

    fun update(requesterId: UUID, id: UUID, documentType: DocumentType?, expiryDate: LocalDate?): UserDocument {
        val doc = getById(requesterId, id) ?: throw NoSuchElementException("Not found")
        documentType?.let { doc.documentType = it }
        expiryDate?.let { doc.expiryDate = it }
        doc.updatedAt = LocalDateTime.now()
        return repository.save(doc)
    }

    fun delete(requesterId: UUID, id: UUID) {
        val doc = getById(requesterId, id) ?: throw NoSuchElementException("Not found")
        repository.softDeleteById(id)
        storage.delete(doc.fileUrl)
    }

    fun verify(requesterId: UUID, id: UUID) {
        val doc = getById(requesterId, id) ?: throw NoSuchElementException("Not found")
        doc.verify()
        repository.save(doc)
    }

    fun getByTaskId(requesterId: UUID, taskId: UUID): List<UserDocument> {
        // For simplicity, we only return docs for task - caller must ensure permission
        return repository.findAllByTaskId(taskId)
    }
}
