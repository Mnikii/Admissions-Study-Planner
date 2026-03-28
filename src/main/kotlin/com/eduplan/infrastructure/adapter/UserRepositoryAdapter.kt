package com.eduplan.infrastructure.adapter

import com.eduplan.application.port.output.UserRepositoryPort
import com.eduplan.domain.model.User
import com.eduplan.infrastructure.mapper.UserMapper
import com.eduplan.infrastructure.repository.UserJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserRepositoryAdapter(
    private val jpaRepository: UserJpaRepository,
    private val mapper: UserMapper,
) : UserRepositoryPort {
    override fun save(user: User): User {
        val entity = mapper.toJpa(user)
        val savedEntity = jpaRepository.save(entity)
        return mapper.toDomain(savedEntity)
    }

    override fun findById(id: UUID): User? = jpaRepository.findByIdOrNull(id)?.let { mapper.toDomain(it) }

    override fun findAll(): List<User> = jpaRepository.findAll().map { mapper.toDomain(it) }

    override fun findByUsername(username: String): User? =
        jpaRepository.findByUsername(username)?.let {
            mapper.toDomain(it)
        }

    override fun findByEmail(email: String): User? = jpaRepository.findByEmail(email)?.let { mapper.toDomain(it) }
}
