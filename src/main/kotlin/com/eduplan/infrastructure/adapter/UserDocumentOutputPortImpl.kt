package com.eduplan.infrastructure.adapter

import com.eduplan.application.port.output.UserDocumentOutputPort
import com.eduplan.domain.model.UserDocument
import com.eduplan.infrastructure.mapper.UserDocumentMapper
import com.eduplan.infrastructure.repository.UserDocumentJpaRepository
import org.springframework.stereotype.Component
import org.springframework.data.repository.findByIdOrNull
import java.util.*

@Component
class UserDocumentOutputPortImpl(
    private val jpa: UserDocumentJpaRepository,
    private val mapper: UserDocumentMapper,
) : UserDocumentOutputPort {

    override fun save(document: UserDocument): UserDocument {
        val entity = mapper.toJpa(document)
        val saved = jpa.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findById(id: UUID): UserDocument? = jpa.findByIdAndDeletedAtIsNull(id)?.let { mapper.toDomain(it) }

    override fun findAllByUserId(userId: UUID): List<UserDocument> = jpa.findAllByUserIdAndDeletedAtIsNull(userId).map { mapper.toDomain(it) }

    override fun findAllByTaskId(taskId: UUID): List<UserDocument> = jpa.findAllByTaskIdAndDeletedAtIsNull(taskId).map { mapper.toDomain(it) }

    override fun softDeleteById(id: UUID) {
        jpa.softDeleteById(id)
    }

    override fun softDeleteByTaskId(taskId: UUID) {
        jpa.softDeleteByTaskId(taskId)
    }
}
