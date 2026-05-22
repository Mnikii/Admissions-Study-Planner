package com.eduplan.domain.repositories

import com.eduplan.domain.model.User
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class StudentRepository {
    private val students = mutableMapOf<UUID, User>()

    fun add(student: User) {
        students[student.id] = student
    }

    fun delete(studentId: UUID) {
        students.remove(studentId)
    }

    fun update(newUser: User) {
        students[newUser.id] = newUser
    }

    fun get(studentId: UUID): User? {
        return students[studentId]
    }
}