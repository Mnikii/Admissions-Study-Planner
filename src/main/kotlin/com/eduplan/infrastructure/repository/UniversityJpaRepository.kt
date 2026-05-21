package com.eduplan.infrastructure.repository

import com.eduplan.infrastructure.entity.UniversityJpaEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UniversityJpaRepository : JpaRepository<UniversityJpaEntity, UUID> {
    @Query("SELECT u FROM UniversityJpaEntity u WHERE u.name = :name")
    fun findByName(@Param("name") name: String): UniversityJpaEntity?
    // fun findByName(name: String): UniversityJpaEntity?

    @Query("SELECT i FROM UniversityJpaEntity i WHERE i.id = :id")
    fun findByIdUni(@Param("id") id: UUID): UniversityJpaEntity?
    // fun findById(id: UUID): Optional<UniversityJpaEntity>

    @Query("SELECT c FROM UniversityJpaEntity c WHERE LOWER(c.country) = LOWER(:county)")
    fun findAllByAddressQuery(@Param("address") address: String): List<UniversityJpaEntity>
    // fun findAllByAddressIgnoreCase(country: String): List<UniversityJpaEntity>

    @Query("SELECT c FROM UniversityJpaEntity c ORDER BY c.name ASC")
    fun findTopByNameAscQuery(pageable: Pageable): List<UniversityJpaEntity>
    // fun findAllByOrderByNameAsc(pageable: Pageable): List<UniversityJpaEntity>
}
