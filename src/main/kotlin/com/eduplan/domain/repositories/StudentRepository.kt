package com.eduplan.domain.repositories

import com.eduplan.domain.model.User
import com.eduplan.domain.services.LoggingService
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class StudentRepository {
    private val logger = LoggingService()
    private val students = mutableMapOf<UUID, User>()

    init {
        logger.info("Student repository created")
    }

    fun add(student: User) {
        students[student.id] = student
    }

    fun delete(studentId: UUID) {
        students.remove(studentId)
    }

    fun update(newStudent: User) {
        students[newStudent.id] = newStudent
    }

    fun get(studentId: UUID): User? {
        return students[studentId]
    }
}