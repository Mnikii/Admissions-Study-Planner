package com.eduplan.infrastructure.repository

import com.eduplan.infrastructure.entity.UserJpaEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserJpaRepository : JpaRepository<UserJpaEntity, UUID> {

    @Query("SELECT u FROM UserJpaEntity u WHERE u.username = :username")
    fun findByUsername(@Param("username") username: String): UserJpaEntity?
    // fun findByUsername(username: String): UserJpaEntity?

    @Query("SELECT e FROM UserJpaEntity e WHERE LOWER(e.email) = LOWER(:email)")
    fun findByEmail(@Param("email") email: String): UserJpaEntity?
    // fun findByEmailIgnoreCase(email: String): UserJpaEntity?

    @Query("SELECT i FROM UserJpaEntity i WHERE i.id = :id")
    fun findByIdUs(@Param("id") id: UUID): UserJpaEntity?
    // fun findById(id: UUID): Optional<UserJpaEntity>

    @Query("SELECT s FROM UserJpaEntity s ORDER BY s.createdAt DESC")
    fun findTopByCreatedAtDesc(pageable: Pageable): List<UserJpaEntity>
    // fun findAllByOrderByCreatedAtDesc(pageable: Pageable): List<UserJpaEntity>
}
