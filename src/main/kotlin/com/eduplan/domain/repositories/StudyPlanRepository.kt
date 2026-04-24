package com.eduplan.domain.repositories

import com.eduplan.domain.model.StudyPlan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

interface StudyPlanRepository : JpaRepository<StudyPlan, UUID> {

    fun findByUserIdAndDeletedAtIsNull(userId: UUID): List<StudyPlan>

    fun findByIdAndUserIdAndDeletedAtIsNull(id: UUID, userId: UUID): Optional<StudyPlan>

    @Modifying
    @Transactional
    @Query("UPDATE StudyPlan s SET s.deletedAt = ?2 WHERE s.id = ?1")
    fun softDelete(id: UUID, now: LocalDateTime)
}