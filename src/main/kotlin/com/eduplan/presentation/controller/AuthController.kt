package com.eduplan.presentation.controller

import com.eduplan.application.port.input.AuthUseCase
import com.eduplan.presentation.dto.RegisterRequestDto
import com.eduplan.presentation.dto.RegisterResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authUseCase: AuthUseCase,
) {
    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequestDto,
    ): ResponseEntity<RegisterResponseDto> {
        val result =
            authUseCase.register(
                AuthUseCase.RegisterCommand(
                    username = request.username,
                    password = request.password,
                    firstName = request.firstName,
                    lastName = request.lastName,
                    email = request.email,
                    phoneNumber = request.phoneNumber,
                    birthday = request.birthday,
                ),
            )

        return ResponseEntity(
            RegisterResponseDto(
                userId = result.userId,
                username = result.username,
                role = result.role,
            ),
            HttpStatus.CREATED,
        )
    }
}