package com.eduplan.application.port.output

import com.eduplan.domain.model.UserDocument
import java.util.*

interface UserDocumentOutputPort {
    fun save(document: UserDocument): UserDocument
    fun findById(id: UUID): UserDocument?
    fun findAllByUserId(userId: UUID): List<UserDocument>
    fun findAllByTaskId(taskId: UUID): List<UserDocument>
    fun softDeleteById(id: UUID)
    fun softDeleteByTaskId(taskId: UUID)
}
