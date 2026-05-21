package com.eduplan.presentation.controller

import com.eduplan.application.port.input.UserUseCase
import com.eduplan.domain.model.StudentStatus
import com.eduplan.domain.model.User
import com.eduplan.presentation.dto.CreateUserRequestDto
import com.eduplan.presentation.dto.UpdateUserRequestDto
import com.eduplan.presentation.mapper.UserPresentationMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.UUID

class UserControllerTest {
    private lateinit var userUseCase: UserUseCase
    private lateinit var mapper: UserPresentationMapper
    private lateinit var controller: UserController

    private val testUser =
        User(
            id = UUID.randomUUID(),
            username = "testuser",
            firstName = "Test",
            lastName = "User",
            email = "test@example.com",
            phoneNumber = "+1234567890",
            birthday = LocalDate.of(1990, 1, 1),
            status = StudentStatus.PENDING_VERIFICATION,
        )

    @BeforeEach
    fun setUp() {
        userUseCase = mockk()
        mapper = UserPresentationMapper()
        controller = UserController(userUseCase, mapper)
    }

    @Test
    fun `createUser should create and return user`() {
        val request =
            CreateUserRequestDto(
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                phoneNumber = "+1234567890",
                birthday = LocalDate.of(1990, 1, 1),
            )

        every { userUseCase.createUser(mapper.toCreateCommand(request)) } returns testUser

        val response = controller.createUser(request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.username).isEqualTo("testuser")
        assertThat(response.body?.firstName).isEqualTo("Test")
        verify { userUseCase.createUser(mapper.toCreateCommand(request)) }
    }

    @Test
    fun `getUserById should return user when found`() {
        every { userUseCase.getUserById(testUser.id) } returns testUser

        val response = controller.getUserById(testUser.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.id).isEqualTo(testUser.id)
        assertThat(response.body?.username).isEqualTo("testuser")
    }

    @Test
    fun `getUserById should return 404 when user not found`() {
        val userId = UUID.randomUUID()
        every { userUseCase.getUserById(userId) } returns null

        val response = controller.getUserById(userId)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isNull()
    }

    @Test
    fun `getAllUsers should return list of users`() {
        every { userUseCase.getAllUsers() } returns listOf(testUser)

        val response = controller.getAllUsers()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).hasSize(1)
        assertThat(response.body?.first()?.username).isEqualTo("testuser")
    }

    @Test
    fun `updateUser should update and return user`() {
        val request =
            UpdateUserRequestDto(
                firstName = "Updated",
                lastName = "User",
                phoneNumber = "+0987654321",
            )
        val command = mapper.toUpdateCommand(request)
        val updatedUser = testUser.copy(firstName = "Updated", phoneNumber = "+0987654321")
        every { userUseCase.updateUser(testUser.id, command) } returns updatedUser

        val response = controller.updateUser(testUser.id, request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.firstName).isEqualTo("Updated")
        assertThat(response.body?.phoneNumber).isEqualTo("+0987654321")
    }

    @Test
    fun `updateUser should return 404 when user not found`() {
        val userId = UUID.randomUUID()
        val request =
            UpdateUserRequestDto(
                firstName = "Updated",
                lastName = "User",
                phoneNumber = "+0987654321",
            )
        val command = mapper.toUpdateCommand(request)

        every { userUseCase.updateUser(userId, command) } throws NoSuchElementException("User not found")

        val response = controller.updateUser(userId, request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `deleteUser should return no content on success`() {
        every { userUseCase.deleteUser(testUser.id) } returns Unit

        val response = controller.deleteUser(testUser.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `deleteUser should return 404 when user not found`() {
        val userId = UUID.randomUUID()
        every { userUseCase.deleteUser(userId) } throws Exception("User not found")

        val response = controller.deleteUser(userId)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}
