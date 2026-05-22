package com.eduplan.domain.services

import com.eduplan.domain.model.StudyPlan
import com.eduplan.domain.model.StudyPlanProgress
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class StudyPlanDomainService {
    fun calculateDeadline(plan: StudyPlan, taskDeadlines: List<LocalDate?>): LocalDate? =
        taskDeadlines.filterNotNull().maxOrNull() ?: plan.deadline

    fun calculateProgress(totalTasks: Int, completedTasks: Int): StudyPlanProgress {
        if (totalTasks <= 0) {
            return StudyPlanProgress(totalTasks = 0, completedTasks = 0, percent = 0)
        }
        val percent = ((completedTasks.toDouble() / totalTasks.toDouble()) * 100).toInt()
        return StudyPlanProgress(
            totalTasks = totalTasks,
            completedTasks = completedTasks,
            percent = percent,
        )
    }
}
