package com.eduplan.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class PlanTaskTest {
    @Test
    fun `markCompleted should set status and completedAt once`() {
        val task =
            PlanTask(
                id = UUID.randomUUID(),
                planId = UUID.randomUUID(),
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
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null,
            )

        task.markCompleted()
        val firstCompletedAt = task.completedAt
        task.markCompleted()

        assertThat(task.status).isEqualTo(TaskStatus.COMPLETED)
        assertThat(task.completedAt).isEqualTo(firstCompletedAt)
    }

    @Test
    fun `isOverdue should detect past deadline for incomplete task`() {
        val task =
            PlanTask(
                id = UUID.randomUUID(),
                planId = UUID.randomUUID(),
                title = "Task",
                description = null,
                taskType = TaskType.OTHER,
                status = TaskStatus.IN_PROGRESS,
                priority = TaskPriority.MEDIUM,
                deadline = LocalDate.of(2026, 1, 1),
                completedAt = null,
                universityId = null,
                programId = null,
                orderIndex = 0,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null,
            )

        val overdue = task.isOverdue(LocalDate.of(2026, 2, 1))

        assertThat(overdue).isTrue()
    }
}

