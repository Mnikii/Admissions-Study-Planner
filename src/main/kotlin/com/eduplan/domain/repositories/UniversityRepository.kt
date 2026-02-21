package com.eduplan.domain.repositories

import com.eduplan.domain.model.University
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UniversitiesRepository {
    private val unis = mutableMapOf<UUID, University>()

    fun add(uni: University) {
        unis[uni.universityId] = uni
    }

    fun delete(universityId: UUID) {
        unis.remove(universityId)
    }

    fun update(newUni: University) {
        unis[newUni.universityId] = newUni
    }

    fun get(universityId: UUID): University? {
        return unis[universityId]
    }
}