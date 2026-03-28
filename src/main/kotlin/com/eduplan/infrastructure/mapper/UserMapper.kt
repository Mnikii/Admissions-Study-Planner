package com.eduplan.infrastructure.mapper

import com.eduplan.domain.model.User
import com.eduplan.infrastructure.entity.UserJpaEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDomain(entity: UserJpaEntity): User =
        User(
            id = entity.id,
            createdAt = entity.createdAt,
            deletedAt = entity.deletedAt,
            username = entity.username,
            firstName = entity.firstName,
            lastName = entity.lastName,
            phoneNumber = entity.phoneNumber,
            email = entity.email,
            birthday = entity.birthday,
            status = entity.status
        )

    fun toJpa(domain: User): UserJpaEntity =
        UserJpaEntity(
            id = domain.id,
            createdAt = domain.createdAt,
            deletedAt = domain.deletedAt,
            username = domain.username,
            firstName = domain.firstName,
            lastName = domain.lastName,
            phoneNumber = domain.phoneNumber,
            email = domain.email,
            birthday = domain.birthday,
            status = domain.status
        )
}
