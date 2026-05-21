package com.eduplan.presentation.mapper

import com.eduplan.domain.model.StudentStatus
import com.eduplan.domain.model.User
import com.eduplan.presentation.dto.CreateUserRequestDto
import com.eduplan.presentation.dto.UpdateUserRequestDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class UserPresentationMapperTest {
    private val mapper = UserPresentationMapper()

    @Test
    fun `toResponseDto should map User to UserResponseDto correctly`() {
        val user =
            User(
                id = UUID.randomUUID(),
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = LocalDate.of(1990, 1, 1),
                status = StudentStatus.PENDING_VERIFICATION,
                createdAt = LocalDateTime.of(2023, 1, 1, 0, 0),
                deletedAt = null,
            )

        val result = mapper.toResponseDto(user)

        assertThat(result.id).isEqualTo(user.id)
        assertThat(result.createdAt).isEqualTo(user.createdAt)
        assertThat(result.deletedAt).isEqualTo(user.deletedAt)
        assertThat(result.username).isEqualTo("testuser")
        assertThat(result.firstName).isEqualTo("Test")
        assertThat(result.lastName).isEqualTo("User")
        assertThat(result.email).isEqualTo("test@example.com")
        assertThat(result.phoneNumber).isEqualTo("+1234567890")
        assertThat(result.birthday).isEqualTo(LocalDate.of(1990, 1, 1))
    }

    @Test
    fun `toCreateCommand should map CreateUserRequestDto to CreateUserCommand correctly`() {
        val dto =
            CreateUserRequestDto(
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = LocalDate.of(1990, 1, 1),
            )

        val result = mapper.toCreateCommand(dto)

        assertThat(result.username).isEqualTo("testuser")
        assertThat(result.firstName).isEqualTo("Test")
        assertThat(result.lastName).isEqualTo("User")
        assertThat(result.email).isEqualTo("test@example.com")
        assertThat(result.phoneNumber).isEqualTo("+1234567890")
        assertThat(result.birthday).isEqualTo(LocalDate.of(1990, 1, 1))
    }

    @Test
    fun `toUpdateCommand should map UpdateUserRequestDto to UpdateUserCommand correctly`() {
        val dto =
            UpdateUserRequestDto(
                firstName = "Updated",
                lastName = "User",
                phoneNumber = "+0987654321",
            )

        val result = mapper.toUpdateCommand(dto)

        assertThat(result.firstName).isEqualTo("Updated")
        assertThat(result.lastName).isEqualTo("User")
        assertThat(result.phoneNumber).isEqualTo("+0987654321")
    }

    @Test
    fun `toResponseDto should handle deleted user correctly`() {
        val deletedAt = LocalDateTime.of(2023, 12, 20, 12, 0)
        val user =
            User(
                id = UUID.randomUUID(),
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = LocalDate.of(1990, 1, 1),
                status = StudentStatus.VERIFIED,
                createdAt = LocalDateTime.of(2023, 1, 1, 0, 0),
                deletedAt = deletedAt,
            )

        val result = mapper.toResponseDto(user)

        assertThat(result.deletedAt).isEqualTo(deletedAt)
    }
}
