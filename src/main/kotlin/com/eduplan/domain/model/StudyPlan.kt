package com.eduplan.domain.model

import com.eduplan.domain.model.DegreeLevel
import com.eduplan.domain.model.PlanStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class StudyPlan(
    val id: UUID,
    val userId: UUID,
    var title: String,
    var targetCountry: String,
    var degreeLevel: DegreeLevel,
    var fieldOfStudy: String,
    var status: PlanStatus,
    var startDate: LocalDate?,
    var deadline: LocalDate?,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    var deletedAt: LocalDateTime?
)