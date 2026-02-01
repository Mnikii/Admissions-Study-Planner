package com.eduplan.infrastructure.persistence.mapper

import com.eduplan.domain.model.Demo
import com.eduplan.domain.service.DemoFactory
import com.eduplan.infrastructure.persistence.entity.DemoJpaEntity
import org.springframework.stereotype.Component

/**
 * Mapper для конвертации между JPA Entity и Domain моделью
 *
 * Разделяет технические модели БД от бизнес-моделей.
 */
@Component
class DemoMapper(
    private val demoFactory: DemoFactory = DemoFactory()
) {

    /**
     * Конвертировать JPA Entity в Domain модель
     */
    fun toDomain(entity: DemoJpaEntity): Demo {
        return demoFactory.restore(
            id = entity.id,
            description = entity.description,
            longDescription = entity.longDescription,
            demoPath = entity.demoPath,
            isActive = entity.isActive,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    /**
     * Конвертировать Domain модель в JPA Entity
     */
    fun toJpa(domain: Demo): DemoJpaEntity {
        return DemoJpaEntity(
            id = domain.id,
            description = domain.description,
            longDescription = domain.longDescription,
            demoPath = domain.demoPath,
            isActive = domain.isActive,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    /**
     * Обновить существующую JPA Entity из Domain модели
     */
    fun updateJpa(entity: DemoJpaEntity, domain: Demo): DemoJpaEntity {
        entity.description = domain.description
        entity.longDescription = domain.longDescription
        entity.demoPath = domain.demoPath
        entity.isActive = domain.isActive
        entity.updatedAt = domain.updatedAt
        return entity
    }

    /**
     * Конвертировать список JPA Entities в Domain модели
     */
    fun toDomainList(entities: List<DemoJpaEntity>): List<Demo> {
        return entities.map { toDomain(it) }
    }
}
