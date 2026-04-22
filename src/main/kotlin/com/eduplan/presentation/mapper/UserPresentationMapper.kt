package com.eduplan.presentation.mapper

import com.eduplan.application.port.input.UserUseCase
import com.eduplan.domain.model.User
import com.eduplan.presentation.dto.CreateUserRequestDto
import com.eduplan.presentation.dto.UpdateUserRequestDto
import com.eduplan.presentation.dto.UserResponseDto
import org.springframework.stereotype.Component

@Component
class UserPresentationMapper {
    fun toResponseDto(user: User): UserResponseDto =
        UserResponseDto(
            id = user.id,
            createdAt = user.createdAt,
            deletedAt = user.deletedAt,
            username = user.username,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            phoneNumber = user.phoneNumber,
            birthday = user.birthday,
        )

    fun toCreateCommand(dto: CreateUserRequestDto): UserUseCase.CreateUserCommand =
        UserUseCase.CreateUserCommand(
            username = dto.username,
            firstName = dto.firstName,
            lastName = dto.lastName,
            email = dto.email,
            phoneNumber = dto.phoneNumber,
            birthday = dto.birthday,
        )

    fun toUpdateCommand(dto: UpdateUserRequestDto): UserUseCase.UpdateUserCommand =
        UserUseCase.UpdateUserCommand(
            firstName = dto.firstName,
            lastName = dto.lastName,
            phoneNumber = dto.phoneNumber,
        )
}