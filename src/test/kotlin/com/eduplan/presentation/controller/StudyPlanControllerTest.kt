package com.eduplan.presentation.controller

import com.eduplan.application.port.input.StudyPlanUseCase
import com.eduplan.domain.model.DegreeLevel
import com.eduplan.domain.model.PlanStatus
import com.eduplan.domain.model.StudyPlan
import com.eduplan.domain.model.StudyPlanProgress
import com.eduplan.presentation.dto.StudyPlanCreateRequest
import com.eduplan.presentation.dto.StudyPlanUpdateRequest
import com.eduplan.presentation.mapper.StudyPlanPresentationMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.util.UUID

class StudyPlanControllerTest {
    private lateinit var useCase: StudyPlanUseCase
    private lateinit var mapper: StudyPlanPresentationMapper
    private lateinit var controller: StudyPlanController

    private val userId = UUID.randomUUID()
    private val plan =
        StudyPlan(
            id = UUID.randomUUID(),
            userId = userId,
            title = "Plan",
            targetCountry = "DE",
            degreeLevel = DegreeLevel.MASTER,
            fieldOfStudy = null,
            status = PlanStatus.DRAFT,
            startDate = null,
            deadline = null,
            createdAt = LocalDateTime.of(2026, 1, 1, 0, 0),
            updatedAt = LocalDateTime.of(2026, 1, 1, 0, 0),
            deletedAt = null,
        )

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        mapper = StudyPlanPresentationMapper()
        controller = StudyPlanController(useCase, mapper)
    }

    @Test
    fun `create should return created plan`() {
        val request =
            StudyPlanCreateRequest(
                title = "Plan",
                targetCountry = "DE",
                degreeLevel = DegreeLevel.MASTER,
                fieldOfStudy = null,
                startDate = null,
            )
        every {
            useCase.create(userId, request.title, request.targetCountry, request.degreeLevel, request.fieldOfStudy, request.startDate)
        } returns plan

        val response = controller.create(userId, request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.title).isEqualTo("Plan")
        verify { useCase.create(userId, request.title, request.targetCountry, request.degreeLevel, request.fieldOfStudy, request.startDate) }
    }

    @Test
    fun `getById should return plan with progress`() {
        val result = StudyPlanUseCase.StudyPlanWithProgress(plan, StudyPlanProgress(0, 0, 0.0), emptyList())
        every { useCase.getWithProgress(plan.id, userId) } returns result

        val response = controller.getById(userId, plan.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.id).isEqualTo(plan.id)
    }

    @Test
    fun `update should return updated plan`() {
        val request = StudyPlanUpdateRequest(title = "Updated")
        val updated = plan.copy(title = "Updated")
        val result = StudyPlanUseCase.StudyPlanWithProgress(updated, StudyPlanProgress(0, 0, 0.0), emptyList())
        every { useCase.update(plan.id, userId, any()) } returns updated
        every { useCase.getWithProgress(plan.id, userId) } returns result

        val response = controller.update(userId, plan.id, request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.title).isEqualTo("Updated")
    }

    @Test
    fun `delete should return no content`() {
        every { useCase.delete(plan.id, userId) } returns Unit

        val response = controller.delete(userId, plan.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }
}

