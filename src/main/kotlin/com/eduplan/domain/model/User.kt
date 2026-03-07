package com.eduplan.domain.model

import java.time.LocalDateTime
import java.time.LocalDate
import java.util.UUID

data class User(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val birthday: LocalDate,
    var status: StudentStatus,
    override val id: UUID,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override var deletedAt: LocalDateTime? = null
) : BaseEntity(id, createdAt, deletedAt)
