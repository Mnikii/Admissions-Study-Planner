package com.eduplan.presentation.controller

import com.eduplan.application.port.input.PlanTaskUseCase
import com.eduplan.presentation.dto.PlanTaskCreateRequest
import com.eduplan.presentation.dto.PlanTaskResponse
import com.eduplan.presentation.dto.PlanTaskUpdateRequest
import com.eduplan.presentation.mapper.PlanTaskPresentationMapper
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/study-plans/{planId}/tasks")
class PlanTaskController(
    private val planTaskUseCase: PlanTaskUseCase,
    private val mapper: PlanTaskPresentationMapper,
) {
    @PostMapping
    fun create(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable planId: UUID,
        @Valid @RequestBody request: PlanTaskCreateRequest,
    ): ResponseEntity<PlanTaskResponse> {
        val data = mapper.toCreateData(request)
        val task = planTaskUseCase.create(planId, userId, data)
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(task))
    }

    @GetMapping
    fun getAll(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable planId: UUID,
    ): ResponseEntity<List<PlanTaskResponse>> {
        val tasks = planTaskUseCase.getAllByPlanId(planId, userId)
        return ResponseEntity.ok(tasks.map { mapper.toResponse(it) })
    }

    @GetMapping("/{taskId}")
    fun getById(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable planId: UUID,
        @PathVariable taskId: UUID,
    ): ResponseEntity<PlanTaskResponse> {
        val task = planTaskUseCase.getById(planId, taskId, userId)
        return ResponseEntity.ok(mapper.toResponse(task))
    }

    @PatchMapping("/{taskId}")
    fun update(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable planId: UUID,
        @PathVariable taskId: UUID,
        @Valid @RequestBody request: PlanTaskUpdateRequest,
    ): ResponseEntity<PlanTaskResponse> {
        planTaskUseCase.getById(planId, taskId, userId)
        val data = mapper.toUpdateData(request)
        val task = planTaskUseCase.update(taskId, userId, data)
        return ResponseEntity.ok(mapper.toResponse(task))
    }

    @DeleteMapping("/{taskId}")
    fun delete(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable planId: UUID,
        @PathVariable taskId: UUID,
    ): ResponseEntity<Void> {
        planTaskUseCase.getById(planId, taskId, userId)
        planTaskUseCase.delete(taskId, userId)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/reorder")
    fun reorder(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable planId: UUID,
        @RequestBody orderMap: Map<UUID, Int>,
    ): ResponseEntity<Void> {
        planTaskUseCase.reorderTasks(planId, userId, orderMap)
        return ResponseEntity.noContent().build()
    }
}
