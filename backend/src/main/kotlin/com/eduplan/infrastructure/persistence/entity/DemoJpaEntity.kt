package com.eduplan.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA Entity для Demo
 *
 * Это техническая модель для работы с базой данных.
 * Содержит аннотации JPA и не содержит бизнес-логики.
 */
@Entity
@Table(name = "demos")
class DemoJpaEntity(

    @Id
    @Column(name = "id", nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "description", nullable = false, length = 255)
    var description: String = "",

    @Column(name = "long_description", nullable = false, length = 5000)
    var longDescription: String = "",

    @Column(name = "demo_path", length = 500)
    var demoPath: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {

    @PreUpdate
    fun onPreUpdate() {
        updatedAt = LocalDateTime.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DemoJpaEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "DemoJpaEntity(id=$id, description='$description', isActive=$isActive)"
    }
}
