package com.eduplan.infrastructure.entity

import com.eduplan.domain.model.TaskPriority
import com.eduplan.domain.model.TaskStatus
import com.eduplan.domain.model.TaskType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "plan_tasks",
    indexes = [
        Index(name = "idx_plan_tasks_plan_id", columnList = "plan_id"),
        Index(name = "idx_plan_tasks_plan_id_status", columnList = "plan_id,status"),
        Index(name = "idx_plan_tasks_deadline", columnList = "deadline"),
    ],
)
data class PlanTaskEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,
    @Column(name = "plan_id", nullable = false)
    val planId: UUID,
    @Column(name = "title", nullable = false)
    val title: String,
    @Column(name = "description")
    val description: String?,
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    val taskType: TaskType,
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: TaskStatus,
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    val priority: TaskPriority,
    @Column(name = "deadline")
    val deadline: LocalDate?,
    @Column(name = "completed_at")
    val completedAt: LocalDate?,
    @Column(name = "university_id")
    val universityId: UUID?,
    @Column(name = "program_id")
    val programId: UUID?,
    @Column(name = "order_index", nullable = false)
    val orderIndex: Int,
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false)
    override var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column
    override var deletedAt: LocalDateTime? = null,
) : BaseAuditJpaEntity(createdAt, deletedAt)

