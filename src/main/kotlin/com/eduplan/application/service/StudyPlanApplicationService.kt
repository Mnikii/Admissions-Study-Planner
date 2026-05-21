package com.eduplan.application.service

import com.eduplan.application.port.input.StudyPlanUseCase
import com.eduplan.application.port.output.StudyPlanOutputPort
import com.eduplan.domain.model.DegreeLevel
import com.eduplan.domain.model.PlanStatus
import com.eduplan.domain.model.StudyPlan
import com.eduplan.domain.services.StudyPlanDomainService
import com.eduplan.presentation.exception.AccessDeniedException
import com.eduplan.presentation.exception.StudyPlanNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class StudyPlanApplicationService(
    private val studyPlanOutputPort: StudyPlanOutputPort,
    private val domainService: StudyPlanDomainService,
) : StudyPlanUseCase {
    private val logger = LoggerFactory.getLogger(StudyPlanApplicationService::class.java)

    override fun create(
        userId: UUID,
        title: String,
        targetCountry: String,
        degreeLevel: DegreeLevel,
        fieldOfStudy: String?,
        startDate: java.time.LocalDate?,
    ): StudyPlan {
        val now = LocalDateTime.now()
        val plan =
            StudyPlan(
                id = UUID.randomUUID(),
                userId = userId,
                title = title,
                targetCountry = targetCountry,
                degreeLevel = degreeLevel,
                fieldOfStudy = fieldOfStudy,
                status = PlanStatus.DRAFT,
                startDate = startDate,
                deadline = null,
                createdAt = now,
                updatedAt = now,
                deletedAt = null,
            )
        val saved = studyPlanOutputPort.save(plan)
        logger.info("Study plan created: planId={}, userId={}", saved.id, saved.userId)
        return saved
    }

    override fun getAllForUser(userId: UUID, statusFilter: PlanStatus?): List<StudyPlan> {
        val plans =
            if (statusFilter == PlanStatus.ARCHIVED) {
                studyPlanOutputPort.findAllByUserIdIncludingArchived(userId)
            } else {
                studyPlanOutputPort.findAllByUserId(userId)
            }
        return if (statusFilter != null) {
            plans.filter { it.status == statusFilter }
        } else {
            plans.filter { it.status != PlanStatus.ARCHIVED }
        }
    }

    override fun getById(planId: UUID, userId: UUID): StudyPlan {
        return getOwnedPlan(planId, userId)
    }

    override fun update(planId: UUID, userId: UUID, updateData: StudyPlanUseCase.StudyPlanUpdateData): StudyPlan {
        val plan = getOwnedPlan(planId, userId)
        val updatedPlan =
            plan.copy(
                title = updateData.title ?: plan.title,
                targetCountry = updateData.targetCountry ?: plan.targetCountry,
                degreeLevel = updateData.degreeLevel ?: plan.degreeLevel,
                fieldOfStudy = updateData.fieldOfStudy ?: plan.fieldOfStudy,
                status = updateData.status ?: plan.status,
                startDate = updateData.startDate ?: plan.startDate,
                deadline = updateData.deadline ?: plan.deadline,
                updatedAt = LocalDateTime.now(),
            )

        validateCompletion(updatedPlan, emptyList())
        val saved = studyPlanOutputPort.save(updatedPlan)
        logger.info("Study plan updated: planId={}, userId={}", saved.id, saved.userId)
        return saved
    }

    override fun delete(planId: UUID, userId: UUID) {
        val plan = getOwnedPlan(planId, userId)
        if (plan.status == PlanStatus.ARCHIVED && plan.deletedAt != null) {
            return
        }
        val archived = plan.copy()
        archived.softDelete()
        studyPlanOutputPort.save(archived)
        logger.info("Study plan archived: planId={}, userId={}", planId, userId)
    }

    override fun recalculateDeadline(planId: UUID) {
        val plan = studyPlanOutputPort.findById(planId) ?: throw StudyPlanNotFoundException(planId)
        val recalculatedDeadline = domainService.calculateDeadline(plan, emptyList())
        val updated = plan.copy(deadline = recalculatedDeadline, updatedAt = LocalDateTime.now())
        studyPlanOutputPort.save(updated)
        logger.info("Study plan deadline recalculated: planId={}", planId)
    }

    override fun getWithProgress(planId: UUID, userId: UUID): StudyPlanUseCase.StudyPlanWithProgress {
        val plan = getOwnedPlan(planId, userId)
        val progress = domainService.calculateProgress(totalTasks = 0, completedTasks = 0)
        return StudyPlanUseCase.StudyPlanWithProgress(
            plan = plan,
            progress = progress,
            tasks = emptyList(),
        )
    }

    private fun getOwnedPlan(planId: UUID, userId: UUID): StudyPlan {
        val plan = studyPlanOutputPort.findById(planId) ?: throw StudyPlanNotFoundException(planId)
        if (plan.userId != userId) {
            throw AccessDeniedException("User $userId has no access to plan $planId")
        }
        return plan
    }

    private fun validateCompletion(plan: StudyPlan, tasks: List<StudyPlanUseCase.StudyPlanTaskSummary>) {
        if (plan.status != PlanStatus.COMPLETED) {
            return
        }
        val totalTasks = tasks.size
        val completedTasks = tasks.count { it.status == "COMPLETED" }
        val progress = domainService.calculateProgress(totalTasks, completedTasks)
        if (progress.totalTasks == 0 || progress.completedTasks != progress.totalTasks) {
            throw IllegalStateException("Cannot complete plan with incomplete tasks")
        }
    }
}
