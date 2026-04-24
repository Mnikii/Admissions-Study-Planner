package com.eduplan.application.service

import com.eduplan.application.port.input.StudyPlanInputPort
import com.eduplan.application.port.output.StudyPlanOutputPort
import com.eduplan.domain.model.DegreeLevel
import com.eduplan.domain.model.PlanStatus
import com.eduplan.domain.model.StudyPlan
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class StudyPlanService(
    private val studyPlanRepository: StudyPlanOutputPort
) : StudyPlanInputPort {

    private fun getPlansWithNearestDeadline(userId: UUID): List<StudyPlan>? {
        val incompleteTasks = studyPlanRepository.findByUserIdAndDeletedAtIsNull(userId)
        if(incompleteTasks.isEmpty()) return emptyList()

        val calculatedDeadline = incompleteTasks.filter{ it.status != PlanStatus.COMPLETED }.mapNotNull { it.deadline }.minOrNull() ?: return emptyList()

        val NearestPlans = incompleteTasks.filter{it.status != PlanStatus.COMPLETED && it.deadline == calculatedDeadline}
        return NearestPlans
    }
    fun calculatePlanProgress(userId: UUID): Double {
        val tasks = studyPlanRepository.findByUserIdAndDeletedAtIsNull(userId)
        if (tasks.isEmpty()) return 0.0
        val completed = tasks.count { it.status == PlanStatus.COMPLETED }
        return (completed.toDouble() / tasks.size) * 100.0
    }
    override fun create(
        userId: UUID,
        title: String,
        targetCountry: String,
        degreeLevel: String,
        fieldOfStudy: String,
        startDate: String?
    ): StudyPlan {
        val plan = StudyPlan(
            id = UUID.randomUUID(),
            userId = userId,
            title = title,
            targetCountry = targetCountry,
            degreeLevel = DegreeLevel.valueOf(degreeLevel),
            fieldOfStudy = fieldOfStudy,
            status = PlanStatus.DRAFT,
            startDate = startDate?.let { LocalDate.parse(it) },
            deadline = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )
        return studyPlanRepository.save(plan)
    }

    override fun getUserPlans(userId: UUID): List<StudyPlan> {
        return studyPlanRepository.findByUserIdAndDeletedAtIsNull(userId)
    }

    override fun getPlanById(userId: UUID, planId: UUID): StudyPlan {
        return studyPlanRepository.findByIdAndUserIdAndDeletedAtIsNull(planId, userId)
            .orElseThrow { RuntimeException("План не найден") }
    }

    override fun update(
        userId: UUID,
        planId: UUID,
        title: String,
        targetCountry: String,
        degreeLevel: String,
        fieldOfStudy: String,
        startDate: String?
        deadline: String?
    ): StudyPlan {
        val plan = studyPlanRepository.findByIdAndUserIdAndDeletedAtIsNull(planId, userId)
            .orElseThrow { RuntimeException("План не найден") }

        val updatedPlan = plan.copy(
            title = title,
            targetCountry = targetCountry,
            degreeLevel = DegreeLevel.valueOf(degreeLevel),
            fieldOfStudy = fieldOfStudy,
            startDate = startDate?.let { LocalDate.parse(it) },
            updatedAt = LocalDateTime.now()
            deadline = deadline
        )
        return studyPlanRepository.save(updatedPlan)
    }

    override fun delete(userId: UUID, planId: UUID) {
        val plan = studyPlanRepository.findByIdAndUserIdAndDeletedAtIsNull(planId, userId)
            .orElseThrow { RuntimeException("План не найден") }
        studyPlanRepository.softDelete(planId, LocalDateTime.now())
    }
}