package com.eduplan.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Student(
    val id: UUID,
    var email: String,
    var fullName: String,
    var phoneNumber: String,
    var status: StudentStatus,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {
}