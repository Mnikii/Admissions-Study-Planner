package com.eduplan.application.usecase

import com.eduplan.application.dto.CreateStudentRequest
import com.eduplan.domain.model.Student
import com.eduplan.domain.model.StudentStatus
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import java.util.UUID

class CreateStudentUseCaseTest {

    private lateinit var studentRepository: StudentRepository
    private lateinit var notificationService: NotificationService
    private lateinit var loggingService: LoggingService
    private lateinit var useCase: CreateStudentUseCase

    @BeforeEach
    fun setUp() {
        studentRepository = mockk()
        notificationService = mockk()
        loggingService = mockk()
        useCase = CreateStudentUseCase(studentRepository, notificationService, loggingService)
    }

    @Test
    fun `should create student successfully`() {
        // Arrange
        val request = CreateStudentRequest(
            email = "test@example.com",
            fullName = "John Doe",
            phoneNumber = "+1234567890"
        )

        val expectedStudent = Student(
            id = UUID.randomUUID(),
            email = request.email,
            fullName = request.fullName,
            phoneNumber = request.phoneNumber,
            status = StudentStatus.PENDING_VERIFICATION,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { studentRepository.save(any()) } returns expectedStudent
        every { notificationService.sendWelcomeEmail(any(), any()) } returns true
        every { loggingService.info(any(), any()) } just Runs

        // Act
        val result = useCase.execute(request)

        // Assert
        assertNotNull(result)
        assertEquals(request.email, result.email)
        assertEquals(request.fullName, result.fullName)
        assertEquals("PENDING_VERIFICATION", result.status)

        verify(exactly = 1) { studentRepository.save(any()) }
        verify(exactly = 1) { notificationService.sendWelcomeEmail(any(), any()) }
    }

    @Test
    fun `should throw exception for invalid email`() {
        // Arrange
        val request = CreateStudentRequest(
            email = "invalid-email",
            fullName = "John Doe",
            phoneNumber = "+1234567890"
        )

        // Act & Assert
        assertThrows(Exception::class.java) {
            useCase.execute(request)
        }
    }
}
