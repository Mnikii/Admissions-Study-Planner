package com.eduplan.infrastructure.adapter

import com.eduplan.application.port.output.UniversityRepositoryPort
import com.eduplan.domain.model.University
import com.eduplan.infrastructure.mapper.UniversityMapper
import com.eduplan.infrastructure.repository.UniversityJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class UniversityRepositoryAdapter(
    private val jpaRepository: UniversityJpaRepository,
    private val mapper: UniversityMapper,
) : UniversityRepositoryPort {
    override fun save(university: University): University {
        val entity = mapper.toJpa(university)
        val savedEntity = jpaRepository.save(entity)
        return mapper.toDomain(savedEntity)
    }

    override fun findById(id: UUID): University? = jpaRepository.findByIdOrNull(id)?.let { mapper.toDomain(it) }

    override fun findAll(): List<University> = jpaRepository.findAll().map { mapper.toDomain(it) }

    override fun findByName(name: String): University? =
        jpaRepository.findByName(name)?.let {
            mapper.toDomain(it)
        }
}

