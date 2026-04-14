package com.eduplan.application.port.output

import com.eduplan.domain.model.University
import com.eduplan.domain.model.User
import java.util.*

interface UniversityRepositoryPort {
    fun save(university: University): University

    fun findById(id: UUID): University?

    fun findAll(): List<University>

    fun findByName(name: String): University?
}
