package com.eduplan.domain.model.dto

import com.eduplan.domain.model.DegreeLevel
import java.time.LocalDate
import java.util.UUID

data class StudyPlanRequest(
    val userId: UUID,
    val title: String,
    val targetCountry: String,
    val degreeLevel: DegreeLevel,
    val fieldOfStudy: String,
    val startDate: LocalDate?
)