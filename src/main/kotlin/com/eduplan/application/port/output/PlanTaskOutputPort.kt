package com.eduplan.application.port.output

import com.eduplan.domain.model.PlanTask
import java.util.UUID

interface PlanTaskOutputPort {
    fun save(task: PlanTask): PlanTask

    fun findById(id: UUID): PlanTask?

    fun findAllByPlanId(planId: UUID): List<PlanTask>

    fun softDeleteById(id: UUID)

    fun softDeleteByPlanId(planId: UUID)
}

