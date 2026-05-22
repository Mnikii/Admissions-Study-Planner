package com.eduplan.domain.services

import com.eduplan.domain.model.PlanTask
import org.springframework.stereotype.Service

@Service
class PlanTaskDomainService {
    fun reorderTasks(tasks: List<PlanTask>, orderMap: Map<java.util.UUID, Int>): List<PlanTask> =
        tasks.map { task ->
            val newIndex = orderMap[task.id]
            if (newIndex != null && newIndex != task.orderIndex) {
                task.copy(orderIndex = newIndex)
            } else {
                task
            }
        }
}

