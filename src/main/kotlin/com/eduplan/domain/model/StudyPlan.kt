package com.eduplan.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class StudyPlan(
    override val id: UUID,
    val userId: UUID,
    var title: String,
    var targetCountry: String,
    var degreeLevel: DegreeLevel,
    var fieldOfStudy: String?,
    var status: PlanStatus,
    var startDate: LocalDate?,
    var deadline: LocalDate?,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    override var deletedAt: LocalDateTime? = null,
) : BaseEntity(id, createdAt, deletedAt) {
    override fun softDelete() {
        deletedAt = LocalDateTime.now()
        status = PlanStatus.ARCHIVED
        updatedAt = LocalDateTime.now()
    }

    override fun isActive(): Boolean = deletedAt == null && status != PlanStatus.ARCHIVED
}
