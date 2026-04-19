package com.eduplan.presentation.controller

import com.eduplan.application.port.input.UserUseCase
import com.eduplan.presentation.dto.UserDto
import com.eduplan.presentation.mapper.UserPresentationMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userUseCase: UserUseCase,
    private val mapper: UserPresentationMapper,
) {
    @PostMapping
    fun createUser(
        @RequestBody request: CreateUserRequestDto,
    ): ResponseEntity<UserResponseDto> {
        val command = mapper.toCreateCommand(request)
        val user = userUseCase.createUser(command)
        val response = mapper.toResponseDto(user)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: UUID,
    ): ResponseEntity<UserResponseDto> {
        val user = userUseCase.getUserById(id)
        return if (user != null) {
            ResponseEntity.ok(mapper.toResponseDto(user))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponseDto>> {
        val users = userUseCase.getAllUsers()
        val response = users.map { mapper.toResponseDto(it) }
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: UUID,
        @RequestBody request: UpdateUserRequestDto,
    ): ResponseEntity<UserResponseDto> {
        val command = mapper.toUpdateCommand(request)
        return try {
            val user = userUseCase.updateUser(id, command)
            ResponseEntity.ok(mapper.toResponseDto(user))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> =
        try {
            userUseCase.deleteUser(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
}
