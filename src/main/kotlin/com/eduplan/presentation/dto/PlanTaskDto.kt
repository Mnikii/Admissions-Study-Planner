package com.eduplan.presentation.dto

import com.eduplan.domain.model.TaskPriority
import com.eduplan.domain.model.TaskStatus
import com.eduplan.domain.model.TaskType
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class PlanTaskCreateRequest(
    @field:NotBlank
    @field:Size(max = 255)
    val title: String,
    @field:Size(max = 2000)
    val description: String? = null,
    val taskType: TaskType,
    val priority: TaskPriority? = null,
    @field:FutureOrPresent
    val deadline: LocalDate? = null,
    val universityId: UUID? = null,
    val programId: UUID? = null,
    val orderIndex: Int? = null,
)

data class PlanTaskUpdateRequest(
    @field:Size(min = 1, max = 255)
    val title: String? = null,
    @field:Size(max = 2000)
    val description: String? = null,
    val taskType: TaskType? = null,
    val status: TaskStatus? = null,
    val priority: TaskPriority? = null,
    @field:FutureOrPresent
    val deadline: LocalDate? = null,
    val universityId: UUID? = null,
    val programId: UUID? = null,
    val orderIndex: Int? = null,
)

data class PlanTaskResponse(
    val id: UUID,
    val planId: UUID,
    val title: String,
    val description: String?,
    val taskType: TaskType,
    val status: TaskStatus,
    val priority: TaskPriority,
    val deadline: LocalDate?,
    val completedAt: LocalDate?,
    val universityId: UUID?,
    val programId: UUID?,
    val orderIndex: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

