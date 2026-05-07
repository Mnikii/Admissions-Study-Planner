package com.eduplan.presentation.mapper

import com.eduplan.application.port.input.UniversityUseCase
import com.eduplan.domain.model.University
import com.eduplan.presentation.dto.CreateUniversityRequestDto
import com.eduplan.presentation.dto.UniversityResponseDto
import com.eduplan.presentation.dto.UpdateUniversityRequestDto
import org.springframework.stereotype.Component

@Component
class UniversityPresentationMapper {
    fun toResponseDto(university: University): UniversityResponseDto =
        UniversityResponseDto(
            id = university.id,
            createdAt = university.createdAt,
            deletedAt = university.deletedAt,
            name = university.name,
            address = university.address,
            country = university.country,
            website = university.website
        )

    fun toCreateCommand(dto: CreateUniversityRequestDto): UniversityUseCase.CreateUniversityCommand =
        UniversityUseCase.CreateUniversityCommand(
            name = dto.name,
            address = dto.address,
            country = dto.country,
            website = dto.website
        )

    fun toUpdateCommand(dto: UpdateUniversityRequestDto): UniversityUseCase.UpdateUniversityCommand =
        UniversityUseCase.UpdateUniversityCommand(
            name = dto.name,
            address = dto.address,
            country = dto.country,
            website = dto.website
        )
}
