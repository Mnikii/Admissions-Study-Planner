package com.eduplan.domain.model.dto

import com.eduplan.domain.model.DegreeLevel
import com.eduplan.domain.model.PlanStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class StudyPlanResponse(
    val id: UUID,
    val title: String,
    val targetCountry: String,
    val degreeLevel: DegreeLevel,
    val fieldOfStudy: String,
    val status: PlanStatus,
    val startDate: LocalDate?,
    val deadline: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)