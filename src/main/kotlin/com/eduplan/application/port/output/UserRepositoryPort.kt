package com.eduplan.application.port.output

import com.eduplan.domain.model.User
import java.util.*

interface UserRepositoryPort {
    fun save(user: User): User

    fun findById(id: UUID): User?

    fun findAll(): List<User>

    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?
}
