package com.eduplan.infrastructure.adapter

import com.eduplan.application.port.output.StudyPlanOutputPort
import com.eduplan.domain.model.PlanStatus
import com.eduplan.domain.model.StudyPlan
import com.eduplan.infrastructure.mapper.StudyPlanMapper
import com.eduplan.infrastructure.repository.StudyPlanJpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class StudyPlanOutputPortImpl(
    private val repository: StudyPlanJpaRepository,
    private val mapper: StudyPlanMapper,
) : StudyPlanOutputPort {
    override fun save(plan: StudyPlan): StudyPlan {
        val entity = mapper.toEntity(plan)
        val saved = repository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findById(id: UUID): StudyPlan? =
        repository.findByIdAndDeletedAtIsNull(id)?.let { mapper.toDomain(it) }

    override fun findAllByUserId(userId: UUID): List<StudyPlan> =
        repository.findAllByUserIdAndDeletedAtIsNull(userId).map { mapper.toDomain(it) }

    override fun existsByIdAndUserId(planId: UUID, userId: UUID): Boolean =
        repository.existsByIdAndUserIdAndDeletedAtIsNull(planId, userId)

    override fun softDelete(planId: UUID) {
        val entity = repository.findById(planId).orElse(null) ?: return
        val archived =
            entity.copy(
                deletedAt = LocalDateTime.now(),
                status = PlanStatus.ARCHIVED,
                updatedAt = LocalDateTime.now(),
            )
        repository.save(archived)
    }

    override fun findAllByUserIdIncludingArchived(userId: UUID): List<StudyPlan> =
        repository.findAllByUserId(userId).map { mapper.toDomain(it) }
}

