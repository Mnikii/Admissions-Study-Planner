package com.eduplan.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class PlanTask(
    override val id: UUID,
    val planId: UUID,
    var title: String,
    var description: String?,
    var taskType: TaskType,
    var status: TaskStatus = TaskStatus.PENDING,
    var priority: TaskPriority = TaskPriority.MEDIUM,
    var deadline: LocalDate?,
    var completedAt: LocalDate?,
    var universityId: UUID?,
    var programId: UUID?,
    var orderIndex: Int,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    override var deletedAt: LocalDateTime? = null,
) : BaseEntity(id, createdAt, deletedAt) {
    fun markCompleted() {
        if (status != TaskStatus.COMPLETED) {
            status = TaskStatus.COMPLETED
            completedAt = LocalDate.now()
        }
    }

    fun isOverdue(currentDate: LocalDate): Boolean =
        status != TaskStatus.COMPLETED && deadline != null && deadline!!.isBefore(currentDate)

    override fun softDelete() {
        deletedAt = LocalDateTime.now()
    }
}

