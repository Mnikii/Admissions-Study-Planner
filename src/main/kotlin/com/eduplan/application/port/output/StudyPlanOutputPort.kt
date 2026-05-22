package com.eduplan.application.port.output

import com.eduplan.domain.model.StudyPlan
import java.util.UUID

interface StudyPlanOutputPort {
    fun save(plan: StudyPlan): StudyPlan

    fun findById(id: UUID): StudyPlan?

    fun findAllByUserId(userId: UUID): List<StudyPlan>

    fun existsByIdAndUserId(planId: UUID, userId: UUID): Boolean

    fun softDelete(planId: UUID)

    fun findAllByUserIdIncludingArchived(userId: UUID): List<StudyPlan>
}

