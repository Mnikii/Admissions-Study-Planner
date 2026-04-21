package com.eduplan.application.port.output


import com.eduplan.domain.model.StudyPlan
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

interface StudyPlanOutputPort {
    fun save(studyPlan: StudyPlan): StudyPlan
    fun findByUserIdAndDeletedAtIsNull(userId: UUID): List<StudyPlan>
    fun findByIdAndUserIdAndDeletedAtIsNull(id: UUID, userId: UUID): Optional<StudyPlan>
    fun softDelete(id: UUID, now: LocalDateTime)
}