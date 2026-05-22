package com.eduplan.infrastructure.mapper

import com.eduplan.domain.model.StudyPlan
import com.eduplan.infrastructure.entity.StudyPlanEntity
import org.springframework.stereotype.Component

@Component
class StudyPlanMapper {
    fun toDomain(entity: StudyPlanEntity): StudyPlan =
        StudyPlan(
            id = entity.id,
            userId = entity.userId,
            title = entity.title,
            targetCountry = entity.targetCountry,
            degreeLevel = entity.degreeLevel,
            fieldOfStudy = entity.fieldOfStudy,
            status = entity.status,
            startDate = entity.startDate,
            deadline = entity.deadline,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            deletedAt = entity.deletedAt,
        )

    fun toEntity(domain: StudyPlan): StudyPlanEntity =
        StudyPlanEntity(
            id = domain.id,
            userId = domain.userId,
            title = domain.title,
            targetCountry = domain.targetCountry,
            degreeLevel = domain.degreeLevel,
            fieldOfStudy = domain.fieldOfStudy,
            status = domain.status,
            startDate = domain.startDate,
            deadline = domain.deadline,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            deletedAt = domain.deletedAt,
        )
}

