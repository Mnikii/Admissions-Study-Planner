package com.eduplan.presentation.controller

import com.eduplan.application.service.UserDocumentApplicationService
import com.eduplan.domain.model.DocumentType
import com.eduplan.presentation.dto.DocumentResponse
import com.eduplan.presentation.dto.DocumentUpdateRequest
import com.eduplan.presentation.mapper.DocumentPresentationMapper
import jakarta.validation.Valid
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.util.*

@RestController
class UserDocumentController(
    private val service: UserDocumentApplicationService,
    private val mapper: DocumentPresentationMapper,
) {

    @PostMapping("/api/v1/documents")
    fun upload(
        @RequestHeader("X-User-Id") userId: UUID,
        @RequestParam("file") file: MultipartFile,
        @RequestParam("documentType") documentType: DocumentType,
        @RequestParam("taskId", required = false) taskId: UUID?,
        @RequestParam("expiryDate", required = false) expiryDate: String?,
    ): ResponseEntity<DocumentResponse> {
        val expiry = expiryDate?.let { LocalDate.parse(it) }
        val saved = service.upload(userId, file.originalFilename ?: file.name, file.contentType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE, file.size, file.inputStream, documentType, taskId, expiry)
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved))
    }

    @GetMapping("/api/v1/documents")
    fun list(
        @RequestHeader("X-User-Id") userId: UUID,
        @RequestParam("type", required = false) type: DocumentType?,
    ): ResponseEntity<List<DocumentResponse>> {
        val docs = service.getAllForUser(userId, userId, type)
        return ResponseEntity.ok(docs.map { mapper.toResponse(it) })
    }

    @GetMapping("/api/v1/documents/{id}")
    fun getById(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable id: UUID,
    ): ResponseEntity<DocumentResponse> {
        val doc = service.getById(userId, id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(mapper.toResponse(doc))
    }

    @GetMapping("/api/v1/documents/{id}/download")
    fun download(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable id: UUID,
    ): ResponseEntity<Resource> {
        val res = service.download(userId, id) ?: return ResponseEntity.notFound().build()
        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$id\"")
        return ResponseEntity.ok().headers(headers).body(res)
    }

    @PatchMapping("/api/v1/documents/{id}")
    fun update(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable id: UUID,
        @Valid @RequestBody request: DocumentUpdateRequest,
    ): ResponseEntity<DocumentResponse> {
        val updated = service.update(userId, id, request.documentType, request.expiryDate)
        return ResponseEntity.ok(mapper.toResponse(updated))
    }

    @DeleteMapping("/api/v1/documents/{id}")
    fun delete(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        service.delete(userId, id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/api/v1/tasks/{taskId}/documents")
    fun getByTask(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable taskId: UUID,
    ): ResponseEntity<List<DocumentResponse>> {
        val docs = service.getByTaskId(userId, taskId)
        return ResponseEntity.ok(docs.map { mapper.toResponse(it) })
    }

    @PostMapping("/api/v1/documents/{id}/verify")
    fun verify(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        service.verify(userId, id)
        return ResponseEntity.noContent().build()
    }
}
