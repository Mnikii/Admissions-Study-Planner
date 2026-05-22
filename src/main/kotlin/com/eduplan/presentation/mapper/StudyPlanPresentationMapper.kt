package com.eduplan.presentation.mapper

import com.eduplan.application.port.input.StudyPlanUseCase
import com.eduplan.domain.model.StudyPlan
import com.eduplan.presentation.dto.StudyPlanProgressDto
import com.eduplan.presentation.dto.StudyPlanResponse
import com.eduplan.presentation.dto.StudyPlanTaskDto
import org.springframework.stereotype.Component

@Component
class StudyPlanPresentationMapper {
    fun toResponse(result: StudyPlanUseCase.StudyPlanWithProgress): StudyPlanResponse =
        toResponse(result.plan, result.progress, result.tasks)

    fun toResponse(plan: StudyPlan, progress: com.eduplan.domain.model.StudyPlanProgress, tasks: List<StudyPlanUseCase.StudyPlanTaskSummary>): StudyPlanResponse =
        StudyPlanResponse(
            id = plan.id,
            title = plan.title,
            targetCountry = plan.targetCountry,
            degreeLevel = plan.degreeLevel,
            fieldOfStudy = plan.fieldOfStudy,
            status = plan.status,
            startDate = plan.startDate,
            deadline = plan.deadline,
            createdAt = plan.createdAt,
            updatedAt = plan.updatedAt,
            progress = StudyPlanProgressDto(
                totalTasks = progress.totalTasks,
                completedTasks = progress.completedTasks,
                completionPercentage = progress.completionPercentage,
            ),
            tasks = tasks.map {
                StudyPlanTaskDto(
                    id = it.id,
                    title = it.title,
                    status = it.status,
                    deadline = it.deadline,
                )
            },
        )
}

