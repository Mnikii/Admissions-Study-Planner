package com.eduplan.presentation.controller

import com.eduplan.application.port.input.StudyPlanUseCase
import com.eduplan.domain.model.PlanStatus
import com.eduplan.domain.model.StudyPlanProgress
import com.eduplan.presentation.dto.StudyPlanCreateRequest
import com.eduplan.presentation.dto.StudyPlanUpdateRequest
import com.eduplan.presentation.dto.StudyPlanResponse
import com.eduplan.presentation.mapper.StudyPlanPresentationMapper
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/study-plans")
class StudyPlanController(
    private val studyPlanUseCase: StudyPlanUseCase,
    private val mapper: StudyPlanPresentationMapper,
) {
    @PostMapping
    fun create(
        @RequestHeader("X-User-Id") userId: UUID,
        @Valid @RequestBody request: StudyPlanCreateRequest,
    ): ResponseEntity<StudyPlanResponse> {
        val plan =
            studyPlanUseCase.create(
                userId = userId,
                title = request.title,
                targetCountry = request.targetCountry,
                degreeLevel = request.degreeLevel,
                fieldOfStudy = request.fieldOfStudy,
                startDate = request.startDate,
            )
        val response = mapper.toResponse(plan, StudyPlanProgress(0, 0, 0), emptyList())
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllForUser(
        @RequestHeader("X-User-Id") userId: UUID,
        @RequestParam(required = false) status: PlanStatus?,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "20") size: Int,
        @RequestParam(required = false) sort: String?,
    ): ResponseEntity<List<StudyPlanResponse>> {
        val plans = studyPlanUseCase.getAllForUser(userId, status)
        val sorted = applySort(plans, sort)
        val paged = applyPagination(sorted, page, size)
        val response = paged.map { mapper.toResponse(it, StudyPlanProgress(0, 0, 0), emptyList()) }
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getById(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable id: UUID,
    ): ResponseEntity<StudyPlanResponse> {
        val result = studyPlanUseCase.getWithProgress(id, userId)
        return ResponseEntity.ok(mapper.toResponse(result))
    }

    @PatchMapping("/{id}")
    fun update(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable id: UUID,
        @Valid @RequestBody request: StudyPlanUpdateRequest,
    ): ResponseEntity<StudyPlanResponse> {
        val updateData =
            StudyPlanUseCase.StudyPlanUpdateData(
                title = request.title,
                targetCountry = request.targetCountry,
                degreeLevel = request.degreeLevel,
                fieldOfStudy = request.fieldOfStudy,
                status = request.status,
                startDate = request.startDate,
                deadline = request.deadline,
            )
        studyPlanUseCase.update(id, userId, updateData)
        val updated = studyPlanUseCase.getWithProgress(id, userId)
        return ResponseEntity.ok(mapper.toResponse(updated))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        studyPlanUseCase.delete(id, userId)
        return ResponseEntity.noContent().build()
    }

    private fun applySort(plans: List<com.eduplan.domain.model.StudyPlan>, sort: String?): List<com.eduplan.domain.model.StudyPlan> {
        if (sort.isNullOrBlank()) {
            return plans
        }
        val parts = sort.split(",")
        val field = parts.getOrNull(0) ?: return plans
        val direction = parts.getOrNull(1)?.lowercase() ?: "asc"
        val comparator = when (field) {
            "createdAt" -> compareBy<com.eduplan.domain.model.StudyPlan> { it.createdAt }
            "updatedAt" -> compareBy<com.eduplan.domain.model.StudyPlan> { it.updatedAt }
            "deadline" -> compareBy<com.eduplan.domain.model.StudyPlan> { it.deadline }
            else -> compareBy<com.eduplan.domain.model.StudyPlan> { it.createdAt }
        }
        val sorted = plans.sortedWith(comparator)
        return if (direction == "desc") sorted.reversed() else sorted
    }

    private fun applyPagination(plans: List<com.eduplan.domain.model.StudyPlan>, page: Int, size: Int): List<com.eduplan.domain.model.StudyPlan> {
        if (size <= 0) {
            return emptyList()
        }
        val fromIndex = page.coerceAtLeast(0) * size
        if (fromIndex >= plans.size) {
            return emptyList()
        }
        val toIndex = (fromIndex + size).coerceAtMost(plans.size)
        return plans.subList(fromIndex, toIndex)
    }
}

