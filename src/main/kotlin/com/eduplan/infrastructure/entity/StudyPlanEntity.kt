package com.eduplan.infrastructure.entity

import com.eduplan.domain.model.DegreeLevel
import com.eduplan.domain.model.PlanStatus
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "study_plans",
    indexes = [
        Index(name = "idx_study_plans_user_id", columnList = "user_id"),
        Index(name = "idx_study_plans_user_id_status", columnList = "user_id,status"),
        Index(name = "idx_study_plans_deleted_at", columnList = "deleted_at"),
    ],
)
data class StudyPlanEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column(name = "user_id", nullable = false)
    val userId: UUID = UUID.randomUUID(),
    @Column(name = "title", nullable = false)
    val title: String = "",
    @Column(name = "target_country", nullable = false)
    val targetCountry: String = "",
    @Enumerated(EnumType.STRING)
    @Column(name = "degree_level", nullable = false)
    val degreeLevel: DegreeLevel = DegreeLevel.BACHELOR,
    @Column(name = "field_of_study")
    val fieldOfStudy: String? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: PlanStatus = PlanStatus.DRAFT,
    @Column(name = "start_date")
    val startDate: LocalDate? = null,
    @Column(name = "deadline")
    val deadline: LocalDate? = null,
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false)
    override var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column
    override var deletedAt: LocalDateTime? = null,
) : BaseAuditJpaEntity(createdAt, deletedAt)

