package com.eduplan.domain.repositories

import com.eduplan.domain.model.Student
import com.eduplan.domain.services.LoggingService
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class StudentRepository {
    private val logger = LoggingService()
    private val students = mutableMapOf<UUID, Student>()

    init {
        logger.info("Student repository created")
    }

    fun add(student: Student) {
        students[student.id] = student
    }

    fun delete(studentId: UUID) {
        students.remove(studentId)
    }

    fun update(newStudent: Student) {
        students[newStudent.id] = newStudent
    }

    fun get(studentId: UUID): Student? {
        return students[studentId]
    }
}