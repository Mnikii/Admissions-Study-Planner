package com.eduplan.application.service

import com.eduplan.application.port.input.StudyPlanUseCase
import com.eduplan.application.port.output.StudyPlanOutputPort
import com.eduplan.domain.model.DegreeLevel
import com.eduplan.domain.model.PlanStatus
import com.eduplan.domain.model.StudyPlan
import com.eduplan.domain.services.StudyPlanDomainService
import com.eduplan.presentation.exception.AccessDeniedException
import com.eduplan.presentation.exception.StudyPlanNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class StudyPlanApplicationServiceTest {
    private lateinit var outputPort: FakeStudyPlanOutputPort
    private lateinit var taskOutputPort: FakePlanTaskOutputPort
    private lateinit var service: StudyPlanApplicationService

    @BeforeEach
    fun setUp() {
        outputPort = FakeStudyPlanOutputPort()
        taskOutputPort = FakePlanTaskOutputPort()
        service = StudyPlanApplicationService(outputPort, StudyPlanDomainService(), taskOutputPort)
    }

    @Test
    fun `create should persist study plan`() {
        val userId = UUID.randomUUID()

        val plan =
            service.create(
                userId = userId,
                title = "Plan",
                targetCountry = "DE",
                degreeLevel = DegreeLevel.MASTER,
                fieldOfStudy = null,
                startDate = LocalDate.of(2026, 1, 1),
            )

        assertThat(plan.userId).isEqualTo(userId)
        assertThat(plan.status).isEqualTo(PlanStatus.DRAFT)
        assertThat(outputPort.savedCount).isEqualTo(1)
    }

    @Test
    fun `getById should return plan for owner`() {
        val plan = outputPort.storePlan(userId = UUID.randomUUID())

        val result = service.getById(plan.id, plan.userId)

        assertThat(result.id).isEqualTo(plan.id)
    }

    @Test
    fun `getById should throw when plan not found`() {
        assertThrows<StudyPlanNotFoundException> {
            service.getById(UUID.randomUUID(), UUID.randomUUID())
        }
    }

    @Test
    fun `getById should throw when user is not owner`() {
        val plan = outputPort.storePlan(userId = UUID.randomUUID())

        assertThrows<AccessDeniedException> {
            service.getById(plan.id, UUID.randomUUID())
        }
    }

    @Test
    fun `update should apply fields and save`() {
        val plan = outputPort.storePlan(userId = UUID.randomUUID())

        val updated =
            service.update(
                plan.id,
                plan.userId,
                StudyPlanUseCase.StudyPlanUpdateData(
                    title = "Updated",
                    targetCountry = null,
                    degreeLevel = DegreeLevel.PHD,
                    fieldOfStudy = "AI",
                    status = PlanStatus.ACTIVE,
                    startDate = null,
                    deadline = null,
                ),
            )

        assertThat(updated.title).isEqualTo("Updated")
        assertThat(updated.degreeLevel).isEqualTo(DegreeLevel.PHD)
        assertThat(outputPort.savedCount).isEqualTo(1)
    }

    @Test
    fun `update should prevent completion when tasks incomplete`() {
        val plan = outputPort.storePlan(userId = UUID.randomUUID())
        taskOutputPort.storeTask(plan.id, status = com.eduplan.domain.model.TaskStatus.PENDING)

        assertThrows<IllegalStateException> {
            service.update(
                plan.id,
                plan.userId,
                StudyPlanUseCase.StudyPlanUpdateData(
                    title = null,
                    targetCountry = null,
                    degreeLevel = null,
                    fieldOfStudy = null,
                    status = PlanStatus.COMPLETED,
                    startDate = null,
                    deadline = null,
                ),
            )
        }
    }

    @Test
    fun `recalculateDeadline should use earliest incomplete task deadline`() {
        val plan = outputPort.storePlan(userId = UUID.randomUUID())
        taskOutputPort.storeTask(plan.id, status = com.eduplan.domain.model.TaskStatus.PENDING, deadline = LocalDate.of(2026, 3, 1))
        taskOutputPort.storeTask(plan.id, status = com.eduplan.domain.model.TaskStatus.COMPLETED, deadline = LocalDate.of(2026, 1, 1))
        taskOutputPort.storeTask(plan.id, status = com.eduplan.domain.model.TaskStatus.IN_PROGRESS, deadline = LocalDate.of(2026, 2, 1))

        service.recalculateDeadline(plan.id)

        val saved = outputPort.findAnyById(plan.id)
        assertThat(saved?.deadline).isEqualTo(LocalDate.of(2026, 2, 1))
    }

    @Test
    fun `delete should archive plan`() {
        val plan = outputPort.storePlan(userId = UUID.randomUUID())

        service.delete(plan.id, plan.userId)

        val saved = outputPort.findAnyById(plan.id)
        assertThat(saved?.status).isEqualTo(PlanStatus.ARCHIVED)
        assertThat(saved?.deletedAt).isNotNull()
    }

    private class FakeStudyPlanOutputPort : StudyPlanOutputPort {
        private val storage = linkedMapOf<UUID, StudyPlan>()
        var savedCount: Int = 0
            private set

        fun storePlan(userId: UUID): StudyPlan {
            val plan =
                StudyPlan(
                    id = UUID.randomUUID(),
                    userId = userId,
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
            storage[plan.id] = plan
            return plan
        }

        override fun save(plan: StudyPlan): StudyPlan {
            savedCount++
            storage[plan.id] = plan
            return plan
        }

        override fun findById(id: UUID): StudyPlan? = storage[id]?.takeIf { it.deletedAt == null }

        override fun findAllByUserId(userId: UUID): List<StudyPlan> =
            storage.values.filter { it.userId == userId && it.deletedAt == null }

        override fun existsByIdAndUserId(planId: UUID, userId: UUID): Boolean =
            storage[planId]?.let { it.userId == userId && it.deletedAt == null } ?: false

        override fun softDelete(planId: UUID) {
            storage[planId]?.let {
                storage[planId] = it.copy(deletedAt = LocalDateTime.now(), status = PlanStatus.ARCHIVED)
            }
        }

        override fun findAllByUserIdIncludingArchived(userId: UUID): List<StudyPlan> =
            storage.values.filter { it.userId == userId }

        fun findAnyById(id: UUID): StudyPlan? = storage[id]
    }

    private class FakePlanTaskOutputPort : com.eduplan.application.port.output.PlanTaskOutputPort {
        private val storage = linkedMapOf<UUID, com.eduplan.domain.model.PlanTask>()

        fun storeTask(
            planId: UUID,
            status: com.eduplan.domain.model.TaskStatus,
            deadline: LocalDate? = null,
        ): com.eduplan.domain.model.PlanTask {
            val task =
                com.eduplan.domain.model.PlanTask(
                    id = UUID.randomUUID(),
                    planId = planId,
                    title = "Task",
                    description = null,
                    taskType = com.eduplan.domain.model.TaskType.OTHER,
                    status = status,
                    priority = com.eduplan.domain.model.TaskPriority.MEDIUM,
                    deadline = deadline,
                    completedAt = null,
                    universityId = null,
                    programId = null,
                    orderIndex = 0,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    deletedAt = null,
                )
            storage[task.id] = task
            return task
        }

        override fun save(task: com.eduplan.domain.model.PlanTask): com.eduplan.domain.model.PlanTask {
            storage[task.id] = task
            return task
        }

        override fun findById(id: UUID): com.eduplan.domain.model.PlanTask? = storage[id]?.takeIf { it.deletedAt == null }

        override fun findAllByPlanId(planId: UUID): List<com.eduplan.domain.model.PlanTask> =
            storage.values.filter { it.planId == planId && it.deletedAt == null }

        override fun softDeleteById(id: UUID) {
            storage[id]?.let { storage[id] = it.copy(deletedAt = LocalDateTime.now()) }
        }

        override fun softDeleteByPlanId(planId: UUID) {
            storage.values
                .filter { it.planId == planId }
                .forEach { task -> storage[task.id] = task.copy(deletedAt = LocalDateTime.now()) }
        }
    }
}
