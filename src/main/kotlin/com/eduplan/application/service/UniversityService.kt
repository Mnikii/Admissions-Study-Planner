package com.eduplan.application.service

import com.eduplan.application.port.input.UniversityUseCase
import com.eduplan.domain.model.University
import com.eduplan.infrastructure.adapter.UniversityRepositoryAdapter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class UniversityService(
    private val universityRepository: UniversityRepositoryAdapter,
) : UniversityUseCase {
    override fun createUniversity(command: UniversityUseCase.CreateUniversityCommand): University {
        val newUniversity =
            University(
                id = UUID.randomUUID(),
                name = command.name,
                address= command.address,
                country= command.country,
                website= command.website,
                createdAt= LocalDateTime.now(),
                deletedAt = null
            )
        return universityRepository.save(newUniversity)
    }

    override fun getUniversityById(id: UUID): University? = universityRepository.findById(id)

    override fun updateUniversity(
        id: UUID,
        command: UniversityUseCase.UpdateUniversityCommand,
    ): University {
        val university = universityRepository.findById(id) ?: throw Exception("University with ID $id not found")

        val updatedUniversity =
            university.copy(
                name = command.name,
                address = command.address,
                country = command.country,
                website = command.website
            )

        return universityRepository.save(updatedUniversity)
    }

    override fun deleteUniversity(id: UUID) {
        val university = universityRepository.findById(id) ?: throw Exception("University with ID $id not found")

        val softDeletedUniversity =
            university.copy(
                deletedAt = LocalDateTime.now(),
            )
        universityRepository.save(softDeletedUniversity)
    }

    override fun getAllUniversities(): List<University> = universityRepository.findAll()

    fun getByName(name: String) : University? = universityRepository.findByName(name)
}