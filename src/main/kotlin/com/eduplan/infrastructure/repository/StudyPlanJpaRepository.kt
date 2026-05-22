package com.eduplan.infrastructure.repository

import com.eduplan.infrastructure.entity.StudyPlanEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StudyPlanJpaRepository : JpaRepository<StudyPlanEntity, UUID> {
    fun findByIdAndDeletedAtIsNull(id: UUID): StudyPlanEntity?

    fun findAllByUserIdAndDeletedAtIsNull(userId: UUID): List<StudyPlanEntity>

    fun existsByIdAndUserIdAndDeletedAtIsNull(id: UUID, userId: UUID): Boolean

    fun findAllByUserId(userId: UUID): List<StudyPlanEntity>
}

