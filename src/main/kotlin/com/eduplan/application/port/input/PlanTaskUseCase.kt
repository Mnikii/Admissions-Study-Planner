package com.eduplan.application.port.input

import com.eduplan.domain.model.PlanTask
import com.eduplan.domain.model.TaskPriority
import com.eduplan.domain.model.TaskStatus
import com.eduplan.domain.model.TaskType
import java.time.LocalDate
import java.util.UUID

interface PlanTaskUseCase {
    fun create(planId: UUID, userId: UUID, data: PlanTaskCreateData): PlanTask

    fun getAllByPlanId(planId: UUID, userId: UUID): List<PlanTask>

    fun getById(planId: UUID, taskId: UUID, userId: UUID): PlanTask

    fun update(taskId: UUID, userId: UUID, data: PlanTaskUpdateData): PlanTask

    fun delete(taskId: UUID, userId: UUID)

    fun reorderTasks(planId: UUID, userId: UUID, orderMap: Map<UUID, Int>)

    data class PlanTaskCreateData(
        val title: String,
        val description: String?,
        val taskType: TaskType,
        val priority: TaskPriority?,
        val deadline: LocalDate?,
        val universityId: UUID?,
        val programId: UUID?,
        val orderIndex: Int?,
    )

    data class PlanTaskUpdateData(
        val title: String?,
        val description: String?,
        val taskType: TaskType?,
        val status: TaskStatus?,
        val priority: TaskPriority?,
        val deadline: LocalDate?,
        val universityId: UUID?,
        val programId: UUID?,
        val orderIndex: Int?,
    )
}

