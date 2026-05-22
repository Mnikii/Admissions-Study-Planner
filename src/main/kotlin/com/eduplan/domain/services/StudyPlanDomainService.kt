package com.eduplan.domain.services

 import com.eduplan.domain.model.PlanTask
import com.eduplan.domain.model.StudyPlan
import com.eduplan.domain.model.StudyPlanProgress
import com.eduplan.domain.model.TaskStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class StudyPlanDomainService {
    fun calculateDeadline(plan: StudyPlan, taskDeadlines: List<LocalDate?>): LocalDate? =
        taskDeadlines.filterNotNull().maxOrNull() ?: plan.deadline

    fun calculateProgress(tasks: List<PlanTask>): StudyPlanProgress {
        val activeTasks = tasks.filter { it.deletedAt == null }
        val totalTasks = activeTasks.size
        val completedTasks = activeTasks.count { it.status == TaskStatus.COMPLETED }
        val completionPercentage =
            if (totalTasks == 0) {
                0.0
            } else {
                val raw = (completedTasks.toDouble() / totalTasks.toDouble()) * 100.0
                BigDecimal(raw).setScale(2, RoundingMode.HALF_UP).toDouble()
            }
        return StudyPlanProgress(
            totalTasks = totalTasks,
            completedTasks = completedTasks,
            completionPercentage = completionPercentage,
        )
    }
}
