package com.eduplan.application.service

import com.eduplan.application.port.input.PlanTaskUseCase
import com.eduplan.application.port.input.StudyPlanUseCase
import com.eduplan.application.port.output.PlanTaskOutputPort
import com.eduplan.application.port.output.StudyPlanOutputPort
import com.eduplan.domain.model.PlanTask
import com.eduplan.domain.model.PlanStatus
import com.eduplan.domain.model.TaskPriority
import com.eduplan.domain.model.TaskStatus
import com.eduplan.domain.services.PlanTaskDomainService
import com.eduplan.presentation.exception.AccessDeniedException
import com.eduplan.presentation.exception.StudyPlanNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class PlanTaskApplicationService(
    private val planTaskOutputPort: PlanTaskOutputPort,
    private val studyPlanOutputPort: StudyPlanOutputPort,
    private val studyPlanUseCase: StudyPlanUseCase,
    private val planTaskDomainService: PlanTaskDomainService,
) : PlanTaskUseCase {
    private val logger = LoggerFactory.getLogger(PlanTaskApplicationService::class.java)

    override fun create(planId: UUID, userId: UUID, data: PlanTaskUseCase.PlanTaskCreateData): PlanTask {
        val plan = getOwnedPlan(planId, userId)
        ensurePlanIsActive(plan.status)

        val orderIndex = data.orderIndex ?: nextOrderIndex(planId)
        val now = LocalDateTime.now()
        val task =
            PlanTask(
                id = UUID.randomUUID(),
                planId = planId,
                title = data.title,
                description = data.description,
                taskType = data.taskType,
                status = TaskStatus.PENDING,
                priority = data.priority ?: TaskPriority.MEDIUM,
                deadline = data.deadline,
                completedAt = null,
                universityId = data.universityId,
                programId = data.programId,
                orderIndex = orderIndex,
                createdAt = now,
                updatedAt = now,
                deletedAt = null,
            )

        val saved = planTaskOutputPort.save(task)
        studyPlanUseCase.recalculateDeadline(planId)
        logger.info("Plan task created: taskId={}, planId={}, userId={}", saved.id, planId, userId)
        return saved
    }

    override fun getAllByPlanId(planId: UUID, userId: UUID): List<PlanTask> {
        getOwnedPlan(planId, userId)
        return planTaskOutputPort.findAllByPlanId(planId)
    }

    override fun getById(planId: UUID, taskId: UUID, userId: UUID): PlanTask {
        getOwnedPlan(planId, userId)
        val task = planTaskOutputPort.findById(taskId) ?: throw com.eduplan.presentation.exception.PlanTaskNotFoundException(taskId)
        if (task.planId != planId) {
            throw com.eduplan.presentation.exception.PlanTaskNotFoundException(taskId)
        }
        return task
    }

    override fun update(taskId: UUID, userId: UUID, data: PlanTaskUseCase.PlanTaskUpdateData): PlanTask {
        val existing = planTaskOutputPort.findById(taskId) ?: throw com.eduplan.presentation.exception.PlanTaskNotFoundException(taskId)
        val plan = getOwnedPlan(existing.planId, userId)
        ensurePlanIsActive(plan.status)

        val updatedStatus = resolveStatus(existing, data.status, data.deadline)
        val updatedTask =
            existing.copy(
                title = data.title ?: existing.title,
                description = data.description ?: existing.description,
                taskType = data.taskType ?: existing.taskType,
                status = updatedStatus,
                priority = data.priority ?: existing.priority,
                deadline = data.deadline ?: existing.deadline,
                completedAt = resolveCompletedAt(existing, updatedStatus),
                universityId = data.universityId ?: existing.universityId,
                programId = data.programId ?: existing.programId,
                orderIndex = data.orderIndex ?: existing.orderIndex,
                updatedAt = LocalDateTime.now(),
            )

        val saved = planTaskOutputPort.save(updatedTask)
        studyPlanUseCase.recalculateDeadline(saved.planId)
        logger.info("Plan task updated: taskId={}, planId={}, userId={}", saved.id, saved.planId, userId)
        return saved
    }

    override fun delete(taskId: UUID, userId: UUID) {
        val existing = planTaskOutputPort.findById(taskId) ?: throw com.eduplan.presentation.exception.PlanTaskNotFoundException(taskId)
        getOwnedPlan(existing.planId, userId)
        planTaskOutputPort.softDeleteById(taskId)
        studyPlanUseCase.recalculateDeadline(existing.planId)
        logger.info("Plan task deleted: taskId={}, planId={}, userId={}", taskId, existing.planId, userId)
    }

    override fun reorderTasks(planId: UUID, userId: UUID, orderMap: Map<UUID, Int>) {
        getOwnedPlan(planId, userId)
        val tasks = planTaskOutputPort.findAllByPlanId(planId)
        val updated = planTaskDomainService.reorderTasks(tasks, orderMap)
        updated.forEach { task ->
            if (task.orderIndex != tasks.first { it.id == task.id }.orderIndex) {
                planTaskOutputPort.save(task.copy(updatedAt = LocalDateTime.now()))
            }
        }
        logger.info("Plan task order updated: planId={}, userId={}, count={}", planId, userId, orderMap.size)
    }

    private fun getOwnedPlan(planId: UUID, userId: UUID): com.eduplan.domain.model.StudyPlan {
        val plan = studyPlanOutputPort.findById(planId) ?: throw StudyPlanNotFoundException(planId)
        if (plan.userId != userId) {
            throw AccessDeniedException("User $userId has no access to plan $planId")
        }
        return plan
    }

    private fun ensurePlanIsActive(status: PlanStatus) {
        if (status == PlanStatus.COMPLETED) {
            throw IllegalStateException("Cannot modify tasks for completed study plan")
        }
    }

    private fun nextOrderIndex(planId: UUID): Int {
        val tasks = planTaskOutputPort.findAllByPlanId(planId)
        val max = tasks.maxOfOrNull { it.orderIndex }
        return (max ?: -1) + 1
    }

    private fun resolveStatus(existing: PlanTask, requested: TaskStatus?, deadline: LocalDate?): TaskStatus {
        val target = requested ?: existing.status
        if (target == TaskStatus.COMPLETED) {
            return target
        }
        val effectiveDeadline = deadline ?: existing.deadline
        return if (effectiveDeadline != null && effectiveDeadline.isBefore(LocalDate.now())) {
            TaskStatus.OVERDUE
        } else {
            target
        }
    }

    private fun resolveCompletedAt(existing: PlanTask, newStatus: TaskStatus): LocalDate? {
        return if (newStatus == TaskStatus.COMPLETED) {
            existing.completedAt ?: LocalDate.now()
        } else {
            null
        }
    }
}

