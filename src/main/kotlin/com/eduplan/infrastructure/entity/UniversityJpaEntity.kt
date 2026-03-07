package com.eduplan.infrastructure.entity

import jakarta.persistence.*

@Entity
@Table(name = "universities")
data class UniversityJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(name = "name", nullable = false, unique = true)
    val name: String,
    @Column(name = "address", nullable = false)
    val address: String,
    @Column(name = "website", nullable = false)
    val website: String,
)