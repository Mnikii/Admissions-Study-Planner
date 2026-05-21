package com.eduplan.presentation.mapper

import com.eduplan.application.port.input.StudyPlanUseCase
import com.eduplan.domain.model.DegreeLevel
import com.eduplan.domain.model.PlanStatus
import com.eduplan.domain.model.StudyPlan
import com.eduplan.domain.model.StudyPlanProgress
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class StudyPlanPresentationMapperTest {
    private val mapper = StudyPlanPresentationMapper()

    @Test
    fun `toResponse should map plan, progress, and tasks`() {
        val plan =
            StudyPlan(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                title = "Plan",
                targetCountry = "DE",
                degreeLevel = DegreeLevel.MASTER,
                fieldOfStudy = "CS",
                status = PlanStatus.ACTIVE,
                startDate = null,
                deadline = null,
                createdAt = LocalDateTime.of(2026, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2026, 1, 2, 0, 0),
                deletedAt = null,
            )
        val progress = StudyPlanProgress(totalTasks = 3, completedTasks = 1, percent = 33)
        val tasks =
            listOf(
                StudyPlanUseCase.StudyPlanTaskSummary(
                    id = UUID.randomUUID(),
                    title = "Task",
                    status = "OPEN",
                    deadline = null,
                ),
            )

        val response = mapper.toResponse(plan, progress, tasks)

        assertThat(response.id).isEqualTo(plan.id)
        assertThat(response.progress.totalTasks).isEqualTo(3)
        assertThat(response.tasks).hasSize(1)
        assertThat(response.tasks.first().title).isEqualTo("Task")
    }
}

