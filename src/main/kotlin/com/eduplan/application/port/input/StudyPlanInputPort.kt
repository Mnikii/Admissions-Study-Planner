package com.eduplan.application.port.input

import com.eduplan.domain.model.StudyPlan
import java.util.UUID

interface StudyPlanInputPort {
    fun create(
        userId: UUID,
        title: String,
        targetCountry: String,
        degreeLevel: String,
        fieldOfStudy: String,
        startDate: String?
    ): StudyPlan

    fun getUserPlans(userId: UUID): List<StudyPlan>
    fun getPlanById(userId: UUID, planId: UUID): StudyPlan
    fun update(
        userId: UUID,
        planId: UUID,
        title: String,
        targetCountry: String,
        degreeLevel: String,
        fieldOfStudy: String,
        startDate: String?
        deadline: String?
    ): StudyPlan
    fun delete(userId: UUID, planId: UUID)
}