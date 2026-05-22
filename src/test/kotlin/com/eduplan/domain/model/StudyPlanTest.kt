package com.eduplan.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class StudyPlanTest {
    @Test
    fun `softDelete should mark plan archived and set deletedAt`() {
        val plan = samplePlan()

        plan.softDelete()

        assertThat(plan.status).isEqualTo(PlanStatus.ARCHIVED)
        assertThat(plan.deletedAt).isNotNull()
        assertThat(plan.updatedAt).isAfterOrEqualTo(plan.createdAt)
    }

    @Test
    fun `isActive should return false for archived or deleted plan`() {
        val plan = samplePlan()

        assertThat(plan.isActive()).isTrue()

        plan.softDelete()
        assertThat(plan.isActive()).isFalse()
    }

    private fun samplePlan(): StudyPlan =
        StudyPlan(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            title = "Plan",
            targetCountry = "DE",
            degreeLevel = DegreeLevel.MASTER,
            fieldOfStudy = null,
            status = PlanStatus.ACTIVE,
            startDate = null,
            deadline = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null,
        )
}

