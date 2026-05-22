package com.eduplan.presentation.controller

import com.eduplan.application.port.input.PlanTaskUseCase
import com.eduplan.domain.model.PlanTask
import com.eduplan.domain.model.TaskPriority
import com.eduplan.domain.model.TaskStatus
import com.eduplan.domain.model.TaskType
import com.eduplan.presentation.dto.PlanTaskCreateRequest
import com.eduplan.presentation.dto.PlanTaskUpdateRequest
import com.eduplan.presentation.mapper.PlanTaskPresentationMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.util.UUID

class PlanTaskControllerTest {
    private lateinit var useCase: PlanTaskUseCase
    private lateinit var mapper: PlanTaskPresentationMapper
    private lateinit var controller: PlanTaskController

    private val userId = UUID.randomUUID()
    private val planId = UUID.randomUUID()
    private val task =
        PlanTask(
            id = UUID.randomUUID(),
            planId = planId,
            title = "Task",
            description = null,
            taskType = TaskType.OTHER,
            status = TaskStatus.PENDING,
            priority = TaskPriority.MEDIUM,
            deadline = null,
            completedAt = null,
            universityId = null,
            programId = null,
            orderIndex = 0,
            createdAt = LocalDateTime.of(2026, 1, 1, 0, 0),
            updatedAt = LocalDateTime.of(2026, 1, 1, 0, 0),
            deletedAt = null,
        )

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        mapper = PlanTaskPresentationMapper()
        controller = PlanTaskController(useCase, mapper)
    }

    @Test
    fun `create should return created task`() {
        val request =
            PlanTaskCreateRequest(
                title = "Task",
                description = null,
                taskType = TaskType.OTHER,
                priority = null,
                deadline = null,
                universityId = null,
                programId = null,
                orderIndex = null,
            )
        every { useCase.create(planId, userId, any()) } returns task

        val response = controller.create(userId, planId, request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.id).isEqualTo(task.id)
        verify { useCase.create(planId, userId, any()) }
    }

    @Test
    fun `update should return updated task`() {
        val request = PlanTaskUpdateRequest(title = "Updated")
        val updated = task.copy(title = "Updated")
        every { useCase.getById(planId, task.id, userId) } returns task
        every { useCase.update(task.id, userId, any()) } returns updated

        val response = controller.update(userId, planId, task.id, request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.title).isEqualTo("Updated")
    }
}

