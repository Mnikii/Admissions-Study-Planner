package com.eduplan.infrastructure.repository

import com.eduplan.infrastructure.entity.UserDocumentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserDocumentJpaRepository : JpaRepository<UserDocumentEntity, UUID> {
    fun findByIdAndDeletedAtIsNull(id: UUID): UserDocumentEntity?

    fun findAllByUserIdAndDeletedAtIsNull(userId: UUID): List<UserDocumentEntity>

    fun findAllByTaskIdAndDeletedAtIsNull(taskId: UUID): List<UserDocumentEntity>

    @Modifying
    @Query("update UserDocumentEntity d set d.deletedAt = current_timestamp where d.id = :id and d.deletedAt is null")
    fun softDeleteById(id: UUID)

    @Modifying
    @Query("update UserDocumentEntity d set d.deletedAt = current_timestamp where d.taskId = :taskId and d.deletedAt is null")
    fun softDeleteByTaskId(taskId: UUID)
}
