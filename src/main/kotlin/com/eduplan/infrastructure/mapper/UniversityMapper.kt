package com.eduplan.infrastructure.mapper

import com.eduplan.domain.model.University
import com.eduplan.infrastructure.entity.UniversityJpaEntity
import org.springframework.stereotype.Component

@Component
class UniversityMapper {
    fun toDomain(entity: UniversityJpaEntity): University =
        University(
            id = entity.id,
            name = entity.name,
            address = entity.address,
            country = entity.country,
            website = entity.website,
            createdAt = entity.createdAt,
            deletedAt = entity.deletedAt
        )

    fun toJpa(domain: University): UniversityJpaEntity =
        UniversityJpaEntity(
            id = domain.id,
            name = domain.name,
            address = domain.address,
            country = domain.country,
            website = domain.website,
            createdAt = domain.createdAt,
            deletedAt = domain.deletedAt
        )
}
