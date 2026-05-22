package com.eduplan.application.port.input

import com.eduplan.domain.model.DegreeLevel
import com.eduplan.domain.model.PlanStatus
import com.eduplan.domain.model.StudyPlan
import com.eduplan.domain.model.StudyPlanProgress
import java.time.LocalDate
import java.util.UUID

interface StudyPlanUseCase {
    fun create(
        userId: UUID,
        title: String,
        targetCountry: String,
        degreeLevel: DegreeLevel,
        fieldOfStudy: String?,
        startDate: LocalDate?,
    ): StudyPlan

    fun getAllForUser(userId: UUID, statusFilter: PlanStatus?): List<StudyPlan>

    fun getById(planId: UUID, userId: UUID): StudyPlan

    fun update(planId: UUID, userId: UUID, updateData: StudyPlanUpdateData): StudyPlan

    fun delete(planId: UUID, userId: UUID)

    fun recalculateDeadline(planId: UUID)

    fun getWithProgress(planId: UUID, userId: UUID): StudyPlanWithProgress

    data class StudyPlanUpdateData(
        val title: String?,
        val targetCountry: String?,
        val degreeLevel: DegreeLevel?,
        val fieldOfStudy: String?,
        val status: PlanStatus?,
        val startDate: LocalDate?,
        val deadline: LocalDate?,
    )

    data class StudyPlanTaskSummary(
        val id: UUID,
        val title: String,
        val status: String,
        val deadline: LocalDate?,
    )

    data class StudyPlanWithProgress(
        val plan: StudyPlan,
        val progress: StudyPlanProgress,
        val tasks: List<StudyPlanTaskSummary>,
    )
}

