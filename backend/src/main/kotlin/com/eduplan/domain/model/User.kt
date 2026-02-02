package com.eduplan.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID


data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val birthday: LocalDate,
    var lastSeen: LocalDateTime,
    val passwordHash: String? = null,
    override val id: UUID,
    override val createdAt: LocalDateTime,
    override var deletedAt: LocalDateTime? = null,
    ) : BaseEntity(id, createdAt, deletedAt) {

    fun getPassword(): String? = passwordHash

    fun getFullName(): String = firstName + " " + lastName

    fun isAccountNonExpired(): Boolean = true

    fun isAccountNonLocked(): Boolean = true

    fun isCredentialsNonExpired(): Boolean = true

    fun isEnabled(): Boolean = true
}