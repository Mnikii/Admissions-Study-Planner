package com.eduplan.infrastructure.persistence.repository

import com.eduplan.infrastructure.persistence.entity.DemoJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Spring Data JPA репозиторий для DemoJpaEntity
 */
@Repository
interface DemoJpaRepository : JpaRepository<DemoJpaEntity, UUID> {

    /**
     * Найти все активные Demo
     */
    fun findByIsActiveTrue(): List<DemoJpaEntity>

    /**
     * Найти по описанию (содержит строку, игнорируя регистр)
     */
    fun findByDescriptionContainingIgnoreCase(description: String): List<DemoJpaEntity>

    /**
     * Проверить существование по описанию
     */
    fun existsByDescription(description: String): Boolean

    /**
     * Пользовательский запрос: найти по части пути
     */
    @Query("SELECT d FROM DemoJpaEntity d WHERE d.demoPath LIKE %:pathPart%")
    fun findByDemoPathContaining(pathPart: String): List<DemoJpaEntity>
}
ому