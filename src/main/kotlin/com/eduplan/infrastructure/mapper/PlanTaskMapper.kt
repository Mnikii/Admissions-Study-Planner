package com.eduplan.infrastructure.mapper

import com.eduplan.domain.model.PlanTask
import com.eduplan.infrastructure.entity.PlanTaskEntity
import org.springframework.stereotype.Component

@Component
class PlanTaskMapper {
    fun toDomain(entity: PlanTaskEntity): PlanTask =
        PlanTask(
            id = entity.id,
            planId = entity.planId,
            title = entity.title,
            description = entity.description,
            taskType = entity.taskType,
            status = entity.status,
            priority = entity.priority,
            deadline = entity.deadline,
            completedAt = entity.completedAt,
            universityId = entity.universityId,
            programId = entity.programId,
            orderIndex = entity.orderIndex,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            deletedAt = entity.deletedAt,
        )

    fun toEntity(domain: PlanTask): PlanTaskEntity =
        PlanTaskEntity(
            id = domain.id,
            planId = domain.planId,
            title = domain.title,
            description = domain.description,
            taskType = domain.taskType,
            status = domain.status,
            priority = domain.priority,
            deadline = domain.deadline,
            completedAt = domain.completedAt,
            universityId = domain.universityId,
            programId = domain.programId,
            orderIndex = domain.orderIndex,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            deletedAt = domain.deletedAt,
        )
}

