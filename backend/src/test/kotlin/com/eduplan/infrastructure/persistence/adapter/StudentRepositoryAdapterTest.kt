package com.eduplan.infrastructure.persistence.adapter

import com.eduplan.domain.model.Student
import com.eduplan.domain.model.StudentStatus
import com.eduplan.infrastructure.persistence.jpa.StudentJpaEntity
import com.eduplan.infrastructure.persistence.jpa.StudentStatusEntity
import io.mockk.mockk
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import java.util.UUID
import java.util.Optional

class StudentRepositoryAdapterTest {

    private lateinit var jpaRepository: StudentJpaRepository
    private lateinit var mapper: StudentMapper
    private lateinit var adapter: StudentRepositoryAdapter

    @BeforeEach
    fun setUp() {
        jpaRepository = mockk()
        mapper = StudentMapper()
        adapter = StudentRepositoryAdapter(jpaRepository, mapper)
    }

    @Test
    fun `should save student successfully`() {
        // Arrange
        val student = Student(
            id = UUID.randomUUID(),
            email = "test@example.com",
            fullName = "John Doe",
            phoneNumber = "+1234567890",
            status = StudentStatus.PENDING_VERIFICATION,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val entity = StudentJpaEntity(
            id = student.id,
            email = student.email,
            fullName = student.fullName,
            phoneNumber = student.phoneNumber,
            status = StudentStatusEntity.PENDING_VERIFICATION,
            createdAt = student.createdAt,
            updatedAt = student.updatedAt
        )

        every { jpaRepository.save(any()) } returns entity

        // Act
        val result = adapter.save(student)

        // Assert
        assertNotNull(result)
        assertEquals(student.email, result.email)
        assertEquals(student.fullName, result.fullName)
    }
}
