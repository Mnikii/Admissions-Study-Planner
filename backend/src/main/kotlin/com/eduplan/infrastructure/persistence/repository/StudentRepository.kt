package com.eduplan.infrastructure.persistence.repository

import com.eduplan.domain.model.Student
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class StudentRepository {
    private val students = mutableMapOf<UUID, Student>()

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