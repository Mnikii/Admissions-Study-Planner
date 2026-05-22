package com.eduplan.infrastructure.adapter

import com.eduplan.application.port.output.AuthUserRepositoryPort
import com.eduplan.domain.model.AuthUser
import com.eduplan.infrastructure.mapper.AuthUserMapper
import com.eduplan.infrastructure.repository.AuthUserJpaRepository
import org.springframework.stereotype.Component

@Component
class AuthUserRepositoryAdapter(
    private val jpaRepository: AuthUserJpaRepository,
    private val mapper: AuthUserMapper,
) : AuthUserRepositoryPort {
    override fun save(user: AuthUser): AuthUser {
        val entity = mapper.toJpa(user)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findByUsername(username: String): AuthUser? =
        jpaRepository.findByUsername(username)?.let { mapper.toDomain(it) }

    override fun existsByUsername(username: String): Boolean = jpaRepository.existsByUsername(username)
}