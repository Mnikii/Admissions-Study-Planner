package com.eduplan.presentation.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class UserDtoTest {
    @Test
    fun `UserResponseDto should be created correctly`() {
        val id = UUID.randomUUID()
        val createdAt = LocalDateTime.of(2023, 1, 1, 0, 0)
        val dto =
            UserResponseDto(
                id = id,
                createdAt = createdAt,
                deletedAt = null,
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = LocalDate.of(1990, 1, 1),
            )

        assertThat(dto.id).isEqualTo(id)
        assertThat(dto.createdAt).isEqualTo(createdAt)
        assertThat(dto.deletedAt).isNull()
        assertThat(dto.username).isEqualTo("testuser")
        assertThat(dto.firstName).isEqualTo("Test")
        assertThat(dto.lastName).isEqualTo("User")
        assertThat(dto.email).isEqualTo("test@example.com")
        assertThat(dto.phoneNumber).isEqualTo("+1234567890")
        assertThat(dto.birthday).isEqualTo(LocalDate.of(1990, 1, 1))
    }

    @Test
    fun `CreateUserRequestDto should be created correctly`() {
        val dto =
            CreateUserRequestDto(
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = LocalDate.of(1990, 1, 1),
            )

        assertThat(dto.username).isEqualTo("testuser")
        assertThat(dto.firstName).isEqualTo("Test")
        assertThat(dto.lastName).isEqualTo("User")
        assertThat(dto.email).isEqualTo("test@example.com")
        assertThat(dto.phoneNumber).isEqualTo("+1234567890")
        assertThat(dto.birthday).isEqualTo(LocalDate.of(1990, 1, 1))
    }

    @Test
    fun `UpdateUserRequestDto should be created correctly`() {
        val dto =
            UpdateUserRequestDto(
                firstName = "Updated",
                lastName = "User",
                phoneNumber = "+0987654321",
            )

        assertThat(dto.firstName).isEqualTo("Updated")
        assertThat(dto.lastName).isEqualTo("User")
        assertThat(dto.phoneNumber).isEqualTo("+0987654321")
    }

    @Test
    fun `UserResponseDto should handle null deletedAt correctly`() {
        val dto =
            UserResponseDto(
                id = UUID.randomUUID(),
                createdAt = LocalDateTime.now(),
                deletedAt = null,
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = LocalDate.of(1990, 1, 1),
            )

        assertThat(dto.deletedAt).isNull()
    }
}
