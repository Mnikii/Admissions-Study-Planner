package com.eduplan.presentation.controller

import com.eduplan.application.port.input.UniversityUseCase
import com.eduplan.presentation.dto.CreateUniversityRequestDto
import com.eduplan.presentation.dto.UpdateUniversityRequestDto
import com.eduplan.presentation.dto.UniversityResponseDto
import com.eduplan.presentation.mapper.UniversityPresentationMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/universities")
class UniversityController(
    private val universityUseCase: UniversityUseCase,
    private val mapper: UniversityPresentationMapper,
) {
    @PostMapping
    fun createUniversity(
        @RequestBody request: CreateUniversityRequestDto,
    ): ResponseEntity<UniversityResponseDto> {
        val command = mapper.toCreateCommand(request)
        val university = universityUseCase.createUniversity(command)
        val response = mapper.toResponseDto(university)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getUniversityById(
        @PathVariable id: UUID,
    ): ResponseEntity<UniversityResponseDto> {
        val university = universityUseCase.getUniversityById(id)
        return if (university != null) {
            ResponseEntity.ok(mapper.toResponseDto(university))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllUniversities(): ResponseEntity<List<UniversityResponseDto>> {
        val universities = universityUseCase.getAllUniversities()
        val response = universities.map { mapper.toResponseDto(it) }
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateUniversity(
        @PathVariable id: UUID,
        @RequestBody request: UpdateUniversityRequestDto,
    ): ResponseEntity<UniversityResponseDto> {
        val command = mapper.toUpdateCommand(request)
        return try {
            val university = universityUseCase.updateUniversity(id, command)
            ResponseEntity.ok(mapper.toResponseDto(university))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUniversity(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> =
        try {
            universityUseCase.deleteUniversity(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
}