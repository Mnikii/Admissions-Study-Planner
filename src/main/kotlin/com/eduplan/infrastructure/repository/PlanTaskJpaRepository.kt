package com.eduplan.infrastructure.repository

import com.eduplan.infrastructure.entity.PlanTaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PlanTaskJpaRepository : JpaRepository<PlanTaskEntity, UUID> {
    fun findByIdAndDeletedAtIsNull(id: UUID): PlanTaskEntity?

    fun findAllByPlanIdAndDeletedAtIsNullOrderByOrderIndexAsc(planId: UUID): List<PlanTaskEntity>

    @Modifying
    @Query("update PlanTaskEntity t set t.deletedAt = current_timestamp, t.updatedAt = current_timestamp where t.planId = :planId and t.deletedAt is null")
    fun softDeleteByPlanId(planId: UUID)
}
