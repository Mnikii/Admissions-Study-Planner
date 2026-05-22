package com.eduplan.infrastructure.mapper

import com.eduplan.domain.model.AuthUser
import com.eduplan.infrastructure.entity.AuthUserJpaEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AuthUserMapper {

    fun toJpa(domain: AuthUser): AuthUserJpaEntity {
        return AuthUserJpaEntity(
            id = domain.id,
            userId = domain.userId,
            username = domain.username,
            passwordHash = domain.passwordHash,
            role = domain.role
        )
    }

    fun toDomain(entity: AuthUserJpaEntity): AuthUser {
        return AuthUser(
            id = entity.id,
            userId = entity.userId,
            username = entity.username,
            passwordHash = entity.passwordHash,
            role = entity.role,
            createdAt = LocalDateTime.now()
        )
    }
}