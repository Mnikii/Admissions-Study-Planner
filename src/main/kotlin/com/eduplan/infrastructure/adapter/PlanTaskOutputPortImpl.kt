package com.eduplan.infrastructure.adapter

import com.eduplan.application.port.output.PlanTaskOutputPort
import com.eduplan.domain.model.PlanTask
import com.eduplan.infrastructure.mapper.PlanTaskMapper
import com.eduplan.infrastructure.repository.PlanTaskJpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class PlanTaskOutputPortImpl(
    private val repository: PlanTaskJpaRepository,
    private val mapper: PlanTaskMapper,
) : PlanTaskOutputPort {
    override fun save(task: PlanTask): PlanTask {
        val entity = mapper.toEntity(task)
        val saved = repository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findById(id: UUID): PlanTask? =
        repository.findByIdAndDeletedAtIsNull(id)?.let { mapper.toDomain(it) }

    override fun findAllByPlanId(planId: UUID): List<PlanTask> =
        repository.findAllByPlanIdAndDeletedAtIsNullOrderByOrderIndexAsc(planId).map { mapper.toDomain(it) }

    override fun softDeleteById(id: UUID) {
        val entity = repository.findById(id).orElse(null) ?: return
        val now = LocalDateTime.now()
        val archived = entity.copy(deletedAt = now, updatedAt = now)
        repository.save(archived)
    }

    override fun softDeleteByPlanId(planId: UUID) {
        repository.softDeleteByPlanId(planId)
    }
}
