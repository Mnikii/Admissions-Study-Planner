package com.eduplan.application.service

import com.eduplan.application.port.input.UserUseCase
import com.eduplan.application.port.output.UserRepositoryPort
import com.eduplan.domain.model.StudentStatus
import com.eduplan.domain.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.util.UUID

class UserServiceTest {
    private lateinit var userRepository: FakeUserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = FakeUserRepository()
        userService = UserService(userRepository)
    }

    @Test
    fun `createUser should create and save new user`() {
        val command =
            UserUseCase.CreateUserCommand(
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = LocalDate.of(1990, 1, 1),
            )

        val result = userService.createUser(command)

        assertThat(result.username).isEqualTo("testuser")
        assertThat(result.firstName).isEqualTo("Test")
        assertThat(result.lastName).isEqualTo("User")
        assertThat(result.status).isEqualTo(StudentStatus.PENDING_VERIFICATION)
        assertThat(userRepository.savedCount).isEqualTo(1)
    }

    @Test
    fun `getUserById should return user when found`() {
        val user = testUser(username = "alice")
        userRepository.put(user)

        val result = userService.getUserById(user.id)

        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `getUserById should return null when user not found`() {
        val result = userService.getUserById(UUID.randomUUID())

        assertThat(result).isNull()
    }

    @Test
    fun `updateUser should update and save user`() {
        val existing = testUser(firstName = "Old", lastName = "Name", phoneNumber = "111")
        userRepository.put(existing)

        val command =
            UserUseCase.UpdateUserCommand(
                firstName = "New",
                lastName = "User",
                phoneNumber = "999",
            )

        val result = userService.updateUser(existing.id, command)

        assertThat(result.firstName).isEqualTo("New")
        assertThat(result.lastName).isEqualTo("User")
        assertThat(result.phoneNumber).isEqualTo("999")
        assertThat(userRepository.savedCount).isEqualTo(1)
    }

    @Test
    fun `updateUser should throw exception when user not found`() {
        val command =
            UserUseCase.UpdateUserCommand(
                firstName = "New",
                lastName = "User",
                phoneNumber = "999",
            )

        assertThrows<Exception> {
            userService.updateUser(UUID.randomUUID(), command)
        }
    }

    @Test
    fun `deleteUser should soft delete active user`() {
        val existing = testUser()
        userRepository.put(existing)

        userService.deleteUser(existing.id)

        val deleted = userRepository.findById(existing.id)
        assertThat(deleted?.deletedAt).isNotNull()
        assertThat(userRepository.savedCount).isEqualTo(1)
    }

    @Test
    fun `deleteUser should throw exception when user not found`() {
        assertThrows<Exception> {
            userService.deleteUser(UUID.randomUUID())
        }
    }

    @Test
    fun `deleteUser should not delete already deleted user`() {
        val deleted = testUser(deleted = true)
        userRepository.put(deleted)

        userService.deleteUser(deleted.id)

        assertThat(userRepository.savedCount).isEqualTo(0)
    }

    @Test
    fun `getAllUsers should return only active users`() {
        userRepository.put(testUser(username = "active", deleted = false))
        userRepository.put(testUser(username = "deleted", deleted = true))

        val result = userService.getAllUsers()

        assertThat(result).hasSize(1)
        assertThat(result.first().username).isEqualTo("active")
    }

    @Test
    fun `getAllUsers should return empty list when no active users`() {
        userRepository.put(testUser(username = "deleted-1", deleted = true))
        userRepository.put(testUser(username = "deleted-2", deleted = true))

        val result = userService.getAllUsers()

        assertThat(result).isEmpty()
    }

    private fun testUser(
        username: String = "testuser",
        firstName: String = "Test",
        lastName: String = "User",
        phoneNumber: String = "+1234567890",
        deleted: Boolean = false,
    ): User =
        User(
            id = UUID.randomUUID(),
            username = username,
            firstName = firstName,
            lastName = lastName,
            email = "$username@example.com",
            phoneNumber = phoneNumber,
            birthday = LocalDate.of(1990, 1, 1),
            status = StudentStatus.VERIFIED,
            deletedAt = if (deleted) java.time.LocalDateTime.now() else null,
        )

    private class FakeUserRepository : UserRepositoryPort {
        private val users = linkedMapOf<UUID, User>()
        var savedCount: Int = 0
            private set

        fun put(user: User) {
            users[user.id] = user
        }

        override fun save(user: User): User {
            savedCount++
            users[user.id] = user
            return user
        }

        override fun findById(id: UUID): User? = users[id]

        override fun findAll(): List<User> = users.values.toList()

        override fun findByUsername(username: String): User? = users.values.find { it.username == username }

        override fun findByEmail(email: String): User? = users.values.find { it.email == email }
    }
}
