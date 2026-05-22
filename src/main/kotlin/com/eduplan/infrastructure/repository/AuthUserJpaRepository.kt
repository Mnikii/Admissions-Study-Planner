package com.eduplan.infrastructure.repository

import com.eduplan.infrastructure.entity.AuthUserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuthUserJpaRepository : JpaRepository<AuthUserJpaEntity, UUID> {
    fun findByUsername(username: String): AuthUserJpaEntity?

    fun existsByUsername(username: String): Boolean
}