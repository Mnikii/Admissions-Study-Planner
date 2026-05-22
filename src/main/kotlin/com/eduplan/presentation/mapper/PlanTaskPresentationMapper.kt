package com.eduplan.presentation.mapper

import com.eduplan.application.port.input.PlanTaskUseCase
import com.eduplan.domain.model.PlanTask
import com.eduplan.presentation.dto.PlanTaskCreateRequest
import com.eduplan.presentation.dto.PlanTaskResponse
import com.eduplan.presentation.dto.PlanTaskUpdateRequest
import org.springframework.stereotype.Component

@Component
class PlanTaskPresentationMapper {
    fun toCreateData(request: PlanTaskCreateRequest): PlanTaskUseCase.PlanTaskCreateData =
        PlanTaskUseCase.PlanTaskCreateData(
            title = request.title,
            description = request.description,
            taskType = request.taskType,
            priority = request.priority,
            deadline = request.deadline,
            universityId = request.universityId,
            programId = request.programId,
            orderIndex = request.orderIndex,
        )

    fun toUpdateData(request: PlanTaskUpdateRequest): PlanTaskUseCase.PlanTaskUpdateData =
        PlanTaskUseCase.PlanTaskUpdateData(
            title = request.title,
            description = request.description,
            taskType = request.taskType,
            status = request.status,
            priority = request.priority,
            deadline = request.deadline,
            universityId = request.universityId,
            programId = request.programId,
            orderIndex = request.orderIndex,
        )

    fun toResponse(task: PlanTask): PlanTaskResponse =
        PlanTaskResponse(
            id = task.id,
            planId = task.planId,
            title = task.title,
            description = task.description,
            taskType = task.taskType,
            status = task.status,
            priority = task.priority,
            deadline = task.deadline,
            completedAt = task.completedAt,
            universityId = task.universityId,
            programId = task.programId,
            orderIndex = task.orderIndex,
            createdAt = task.createdAt,
            updatedAt = task.updatedAt,
        )
}

