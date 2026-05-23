package com.eduplan.presentation.controller

import com.eduplan.application.port.input.AuthUseCase
import com.eduplan.presentation.dto.LoginRequestDto
import com.eduplan.presentation.dto.LoginResponseDto
import com.eduplan.presentation.dto.ErrorResponseDto
import com.eduplan.presentation.dto.MeResponseDto
import com.eduplan.presentation.dto.RegisterRequestDto
import com.eduplan.presentation.dto.RegisterResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.GetMapping
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
    @Operation(summary = "Register new user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User registered"),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))],
            ),
        ],
    )
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

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Authenticated"),
            ApiResponse(
                responseCode = "401",
                description = "Invalid credentials",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))],
            ),
        ],
    )
    fun login(
        @RequestBody request: LoginRequestDto,
    ): ResponseEntity<LoginResponseDto> {
        val result =
            authUseCase.login(
                AuthUseCase.LoginCommand(
                    username = request.username,
                    password = request.password,
                ),
            )

        return ResponseEntity.ok(
            LoginResponseDto(
                userId = result.userId,
                username = result.username,
                role = result.role,
                accessToken = result.accessToken,
                tokenType = result.tokenType,
                expiresAtEpochMillis = result.expiresAtEpochMillis,
            ),
        )
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Current user info"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))],
            ),
        ],
    )
    fun me(authentication: Authentication): ResponseEntity<MeResponseDto> {
        val result = authUseCase.me(authentication.name)
        return ResponseEntity.ok(
            MeResponseDto(
                userId = result.userId,
                username = result.username,
                role = result.role,
                firstName = result.firstName,
                lastName = result.lastName,
                email = result.email,
                phoneNumber = result.phoneNumber,
                birthday = result.birthday,
            ),
        )
    }
}