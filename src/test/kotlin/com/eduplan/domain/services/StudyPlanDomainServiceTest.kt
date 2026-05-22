package com.eduplan.domain.services

import com.eduplan.domain.model.PlanTask
import com.eduplan.domain.model.TaskPriority
import com.eduplan.domain.model.TaskStatus
import com.eduplan.domain.model.TaskType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class StudyPlanDomainServiceTest {
    private val service = StudyPlanDomainService()

    @Test
    fun `calculateProgress returns zeros for empty task list`() {
        val progress = service.calculateProgress(emptyList())

        assertThat(progress.totalTasks).isEqualTo(0)
        assertThat(progress.completedTasks).isEqualTo(0)
        assertThat(progress.completionPercentage).isEqualTo(0.0)
    }

    @Test
    fun `calculateProgress returns percent for completed tasks`() {
        val tasks =
            listOf(
                task(TaskStatus.COMPLETED),
                task(TaskStatus.PENDING),
                task(TaskStatus.IN_PROGRESS),
            )

        val progress = service.calculateProgress(tasks)

        assertThat(progress.totalTasks).isEqualTo(3)
        assertThat(progress.completedTasks).isEqualTo(1)
        assertThat(progress.completionPercentage).isEqualTo(33.33)
    }

    @Test
    fun `calculateProgress ignores soft deleted tasks`() {
        val deletedAt = LocalDateTime.of(2026, 1, 5, 10, 0)
        val tasks =
            listOf(
                task(TaskStatus.COMPLETED, deletedAt = deletedAt),
                task(TaskStatus.PENDING),
            )

        val progress = service.calculateProgress(tasks)

        assertThat(progress.totalTasks).isEqualTo(1)
        assertThat(progress.completedTasks).isEqualTo(0)
        assertThat(progress.completionPercentage).isEqualTo(0.0)
    }

    private fun task(status: TaskStatus, deletedAt: LocalDateTime? = null): PlanTask {
        val now = LocalDateTime.of(2026, 1, 1, 9, 0)
        return PlanTask(
            id = UUID.randomUUID(),
            planId = UUID.randomUUID(),
            title = "Task",
            description = null,
            taskType = TaskType.OTHER,
            status = status,
            priority = TaskPriority.MEDIUM,
            deadline = LocalDate.of(2026, 2, 1),
            completedAt = null,
            universityId = null,
            programId = null,
            orderIndex = 0,
            createdAt = now,
            updatedAt = now,
            deletedAt = deletedAt,
        )
    }
}

