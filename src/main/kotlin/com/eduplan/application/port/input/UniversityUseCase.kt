package com.eduplan.application.port.input

import com.eduplan.domain.model.University
import java.util.*

interface UniversityUseCase {
    fun createUniversity(command: CreateUniversityCommand): University

    fun getUniversityById(id: UUID): University?

    fun updateUniversity(
        id: UUID,
        command: UpdateUniversityCommand,
    ): University

    fun deleteUniversity(id: UUID)

    fun getAllUniversities(): List<University>


    data class CreateUniversityCommand(
        val name: String,
        val address: String,
        val country: String,
        val website: String
    )

    data class UpdateUniversityCommand(
        val name: String,
        val address: String,
        val country: String,
        val website: String
    )
}
