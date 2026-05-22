package com.eduplan.application.service

import com.eduplan.application.port.input.PlanTaskUseCase
import com.eduplan.application.port.input.StudyPlanUseCase
import com.eduplan.application.port.output.PlanTaskOutputPort
import com.eduplan.application.port.output.StudyPlanOutputPort
import com.eduplan.domain.model.PlanStatus
import com.eduplan.domain.model.PlanTask
import com.eduplan.domain.model.TaskPriority
import com.eduplan.domain.model.TaskStatus
import com.eduplan.domain.model.TaskType
import com.eduplan.domain.services.PlanTaskDomainService
import com.eduplan.presentation.exception.AccessDeniedException
import com.eduplan.presentation.exception.StudyPlanNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class PlanTaskApplicationServiceTest {
    private lateinit var planTaskOutputPort: FakePlanTaskOutputPort
    private lateinit var studyPlanOutputPort: FakeStudyPlanOutputPort
    private lateinit var studyPlanUseCase: FakeStudyPlanUseCase
    private lateinit var service: PlanTaskApplicationService

    @BeforeEach
    fun setUp() {
        planTaskOutputPort = FakePlanTaskOutputPort()
        studyPlanOutputPort = FakeStudyPlanOutputPort()
        studyPlanUseCase = FakeStudyPlanUseCase()
        service = PlanTaskApplicationService(planTaskOutputPort, studyPlanOutputPort, studyPlanUseCase, PlanTaskDomainService())
    }

    @Test
    fun `create should save task and recalculate deadline`() {
        val userId = UUID.randomUUID()
        val plan = studyPlanOutputPort.storePlan(userId)

        val created =
            service.create(
                plan.id,
                userId,
                PlanTaskUseCase.PlanTaskCreateData(
                    title = "Task",
                    description = null,
                    taskType = TaskType.DOCUMENT,
                    priority = null,
                    deadline = LocalDate.of(2026, 2, 1),
                    universityId = null,
                    programId = null,
                    orderIndex = null,
                ),
            )

        assertThat(created.planId).isEqualTo(plan.id)
        assertThat(planTaskOutputPort.savedCount).isEqualTo(1)
        assertThat(studyPlanUseCase.recalculateCalls).isEqualTo(1)
    }

    @Test
    fun `create should deny access for non-owner`() {
        val plan = studyPlanOutputPort.storePlan(UUID.randomUUID())

        assertThrows<AccessDeniedException> {
            service.create(
                plan.id,
                UUID.randomUUID(),
                PlanTaskUseCase.PlanTaskCreateData(
                    title = "Task",
                    description = null,
                    taskType = TaskType.DOCUMENT,
                    priority = null,
                    deadline = null,
                    universityId = null,
                    programId = null,
                    orderIndex = null,
                ),
            )
        }
    }

    @Test
    fun `update should set completedAt when status becomes completed`() {
        val userId = UUID.randomUUID()
        val plan = studyPlanOutputPort.storePlan(userId)
        val task = planTaskOutputPort.storeTask(plan.id, status = TaskStatus.IN_PROGRESS)

        val updated =
            service.update(
                task.id,
                userId,
                PlanTaskUseCase.PlanTaskUpdateData(
                    title = null,
                    description = null,
                    taskType = null,
                    status = TaskStatus.COMPLETED,
                    priority = null,
                    deadline = null,
                    universityId = null,
                    programId = null,
                    orderIndex = null,
                ),
            )

        assertThat(updated.status).isEqualTo(TaskStatus.COMPLETED)
        assertThat(updated.completedAt).isNotNull()
    }

    @Test
    fun `getById should throw when plan not found`() {
        assertThrows<StudyPlanNotFoundException> {
            service.getById(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        }
    }

    private class FakePlanTaskOutputPort : PlanTaskOutputPort {
        private val storage = linkedMapOf<UUID, PlanTask>()
        var savedCount: Int = 0
            private set

        fun storeTask(planId: UUID, status: TaskStatus, deadline: LocalDate? = null): PlanTask {
            val task =
                PlanTask(
                    id = UUID.randomUUID(),
                    planId = planId,
                    title = "Task",
                    description = null,
                    taskType = TaskType.OTHER,
                    status = status,
                    priority = TaskPriority.MEDIUM,
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

        override fun save(task: PlanTask): PlanTask {
            savedCount++
            storage[task.id] = task
            return task
        }

        override fun findById(id: UUID): PlanTask? = storage[id]?.takeIf { it.deletedAt == null }

        override fun findAllByPlanId(planId: UUID): List<PlanTask> =
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

    private class FakeStudyPlanOutputPort : StudyPlanOutputPort {
        private val storage = linkedMapOf<UUID, com.eduplan.domain.model.StudyPlan>()

        fun storePlan(userId: UUID): com.eduplan.domain.model.StudyPlan {
            val plan =
                com.eduplan.domain.model.StudyPlan(
                    id = UUID.randomUUID(),
                    userId = userId,
                    title = "Plan",
                    targetCountry = "DE",
                    degreeLevel = com.eduplan.domain.model.DegreeLevel.MASTER,
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

        override fun save(plan: com.eduplan.domain.model.StudyPlan): com.eduplan.domain.model.StudyPlan {
            storage[plan.id] = plan
            return plan
        }

        override fun findById(id: UUID): com.eduplan.domain.model.StudyPlan? = storage[id]?.takeIf { it.deletedAt == null }

        override fun findAllByUserId(userId: UUID): List<com.eduplan.domain.model.StudyPlan> =
            storage.values.filter { it.userId == userId && it.deletedAt == null }

        override fun existsByIdAndUserId(planId: UUID, userId: UUID): Boolean =
            storage[planId]?.let { it.userId == userId && it.deletedAt == null } ?: false

        override fun softDelete(planId: UUID) {
            storage[planId]?.let { storage[planId] = it.copy(deletedAt = LocalDateTime.now(), status = PlanStatus.ARCHIVED) }
        }

        override fun findAllByUserIdIncludingArchived(userId: UUID): List<com.eduplan.domain.model.StudyPlan> =
            storage.values.filter { it.userId == userId }
    }

    private class FakeStudyPlanUseCase : StudyPlanUseCase {
        var recalculateCalls: Int = 0
            private set

        override fun create(
            userId: UUID,
            title: String,
            targetCountry: String,
            degreeLevel: com.eduplan.domain.model.DegreeLevel,
            fieldOfStudy: String?,
            startDate: LocalDate?,
        ): com.eduplan.domain.model.StudyPlan {
            throw UnsupportedOperationException()
        }

        override fun getAllForUser(userId: UUID, statusFilter: PlanStatus?): List<com.eduplan.domain.model.StudyPlan> {
            throw UnsupportedOperationException()
        }

        override fun getById(planId: UUID, userId: UUID): com.eduplan.domain.model.StudyPlan {
            throw UnsupportedOperationException()
        }

        override fun update(
            planId: UUID,
            userId: UUID,
            updateData: StudyPlanUseCase.StudyPlanUpdateData,
        ): com.eduplan.domain.model.StudyPlan {
            throw UnsupportedOperationException()
        }

        override fun delete(planId: UUID, userId: UUID) {
            throw UnsupportedOperationException()
        }

        override fun recalculateDeadline(planId: UUID) {
            recalculateCalls++
        }

        override fun getWithProgress(planId: UUID, userId: UUID): StudyPlanUseCase.StudyPlanWithProgress {
            throw UnsupportedOperationException()
        }
    }
}

