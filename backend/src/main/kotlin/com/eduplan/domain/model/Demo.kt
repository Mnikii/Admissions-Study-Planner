package com.eduplan.domain.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Доменная модель Demo (демонстрационная сущность)
 *
 * Это чистая бизнес-модель без зависимостей от фреймворков.
 * Содержит бизнес-логику и инварианты предметной области.
 */
data class Demo(
    val id: UUID,
    val description: String,
    val longDescription: String,
    val demoPath: String?,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    init {
        require(description.isNotBlank()) { "Description cannot be blank" }
        require(description.length <= 255) { "Description cannot exceed 255 characters" }
        require(longDescription.isNotBlank()) { "Long description cannot be blank" }
        require(longDescription.length <= 5000) { "Long description cannot exceed 5000 characters" }
        demoPath?.let {
            require(it.isNotBlank()) { "Demo path cannot be blank if provided" }
        }
    }

    /**
     * Обновление описания с валидацией
     */
    fun updateDescription(newDescription: String, newLongDescription: String): Demo {
        require(newDescription.isNotBlank()) { "Description cannot be blank" }
        require(newLongDescription.isNotBlank()) { "Long description cannot be blank" }

        return copy(
            description = newDescription,
            longDescription = newLongDescription,
            updatedAt = LocalDateTime.now()
        )
    }

    /**
     * Деактивация demo
     */
    fun deactivate(): Demo = copy(
        isActive = false,
        updatedAt = LocalDateTime.now()
    )

    /**
     * Активация demo
     */
    fun activate(): Demo = copy(
        isActive = true,
        updatedAt = LocalDateTime.now()
    )
}
