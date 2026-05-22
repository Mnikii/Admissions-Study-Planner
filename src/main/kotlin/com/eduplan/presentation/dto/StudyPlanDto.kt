package com.eduplan.presentation.dto

import com.eduplan.domain.model.DegreeLevel
import com.eduplan.domain.model.PlanStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class StudyPlanCreateRequest(
    @field:NotBlank
    @field:Size(max = 255)
    val title: String,
    @field:NotBlank
    @field:Size(max = 100)
    val targetCountry: String,
    @field:NotNull
    val degreeLevel: DegreeLevel,
    @field:Size(max = 255)
    val fieldOfStudy: String? = null,
    val startDate: LocalDate? = null,
)

data class StudyPlanUpdateRequest(
    @field:Size(min = 1, max = 255)
    val title: String? = null,
    @field:Size(min = 1, max = 100)
    val targetCountry: String? = null,
    val degreeLevel: DegreeLevel? = null,
    @field:Size(max = 255)
    val fieldOfStudy: String? = null,
    val status: PlanStatus? = null,
    val startDate: LocalDate? = null,
    val deadline: LocalDate? = null,
)

data class StudyPlanProgressDto(
    val totalTasks: Int,
    val completedTasks: Int,
    val completionPercentage: Double,
)

data class StudyPlanTaskDto(
    val id: UUID,
    val title: String,
    val status: String,
    val deadline: LocalDate?,
)

data class StudyPlanResponse(
    val id: UUID,
    val title: String,
    val targetCountry: String,
    val degreeLevel: DegreeLevel,
    val fieldOfStudy: String?,
    val status: PlanStatus,
    val startDate: LocalDate?,
    val deadline: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val progress: StudyPlanProgressDto,
    val tasks: List<StudyPlanTaskDto>,
)
