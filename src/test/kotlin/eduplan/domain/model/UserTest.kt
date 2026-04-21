package com.eduplan.domain.model

import com.eduplan.domain.model.StudentStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class UserTest {
    @Test
    fun `should create user with all required parameters`() {
        val userId = UUID.randomUUID()
        val birthday = LocalDate.of(1990, 1, 1)
        val user =
            User(
                id = userId,
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = birthday,
                status = StudentStatus.PENDING_VERIFICATION,
            )

        assertThat(user.id).isEqualTo(userId)
        assertThat(user.username).isEqualTo("testuser")
        assertThat(user.firstName).isEqualTo("Test")
        assertThat(user.lastName).isEqualTo("User")
        assertThat(user.email).isEqualTo("test@example.com")
        assertThat(user.phoneNumber).isEqualTo("+1234567890")
        assertThat(user.birthday).isEqualTo(birthday)
        assertThat(user.status).isEqualTo(StudentStatus.PENDING_VERIFICATION)
    }

    @Test
    fun `should create user with default values`() {
        val birthday = LocalDate.of(1990, 1, 1)
        val userId = UUID.randomUUID()
        val user =
            User(
                id = userId,
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = birthday,
                status = StudentStatus.VERIFIED,
            )

        assertThat(user.id).isNotNull()
        assertThat(user.createdAt).isNotNull()
        assertThat(user.deletedAt).isNull()
    }

    @Test
    fun `should support data class operations`() {
        val birthday = LocalDate.of(1990, 1, 1)
        val userId = UUID.randomUUID()
        val user1 =
            User(
                id = userId,
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = birthday,
                status = StudentStatus.PENDING_VERIFICATION,
            )
        val user2 = user1.copy(username = "newuser")

        assertThat(user1.username).isEqualTo("testuser")
        assertThat(user2.username).isEqualTo("newuser")
        assertThat(user1).isNotEqualTo(user2)
    }

    @Test
    fun `should handle soft delete correctly`() {
        val birthday = LocalDate.of(1990, 1, 1)
        val userId = UUID.randomUUID()
        val user =
            User(
                id = userId,
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = birthday,
                status = StudentStatus.PENDING_VERIFICATION,
            )

        assertThat(user.deletedAt).isNull()

        val deletedUser = user.copy(deletedAt = LocalDateTime.now())
        assertThat(deletedUser.deletedAt).isNotNull()
    }

    @Test
    fun `should have correct student status`() {
        val birthday = LocalDate.of(1990, 1, 1)
        val userId = UUID.randomUUID()
        val user =
            User(
                id = userId,
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = birthday,
                status = StudentStatus.VERIFIED,
            )

        assertThat(user.status).isEqualTo(StudentStatus.VERIFIED)
    }

}
