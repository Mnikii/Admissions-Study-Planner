package com.eduplan.infrastructure.repository

import com.eduplan.infrastructure.entity.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserJpaRepository: JpaRepository<UserJpaEntity, UUID> {
    fun findByUsername(username: String): UserJpaEntity?

    fun findByEmail(email: String): UserJpaEntity?
}