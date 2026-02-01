package com.eduplan.application.service

import com.eduplan.application.port.input.CreateDemoUseCase
import com.eduplan.application.port.input.UpdateDemoUseCase
import com.eduplan.application.port.output.DemoRepositoryPort
import com.eduplan.domain.exception.EntityNotFoundException
import com.eduplan.domain.model.Demo
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID

/**
 * Unit тесты для DemoService
 *
 * Используем MockK для мокирования зависимостей.
 * Тестируем бизнес-логику без обращения к реальной БД.
 */
class DemoServiceTest {

    private lateinit var demoRepository: DemoRepositoryPort
    private lateinit var demoService: DemoService

    @BeforeEach
    fun setUp() {
        demoRepository = mockk()
        demoService = DemoService(demoRepository)
    }

    @Test
    fun `should create demo successfully`() {
        // given
        val command = CreateDemoUseCase.CreateDemoCommand(
            description = "New Demo",
            longDescription = "Detailed description of new demo",
            demoPath = "/demos/new.pdf",
            isActive = true
        )

        every { demoRepository.save(any()) } answers { firstArg() }

        // when
        val result = demoService.create(command)

        // then
        assertNotNull(result)
        assertEquals("New Demo", result.description)
        assertEquals("Detailed description of new demo", result.longDescription)
        assertEquals("/demos/new.pdf", result.demoPath)
        assertTrue(result.isActive)
        verify(exactly = 1) { demoRepository.save(any()) }
    }

    @Test
    fun `should get demo by id successfully`() {
        // given
        val demoId = UUID.randomUUID()
        val now = LocalDateTime.now()
        val expectedDemo = Demo(
            id = demoId,
            description = "Test Demo",
            longDescription = "Test long description",
            demoPath = null,
            isActive = true,
            createdAt = now,
            updatedAt = now
        )

        every { demoRepository.findById(demoId) } returns expectedDemo

        // when
        val result = demoService.getById(demoId)

        // then
        assertEquals(expectedDemo, result)
        verify(exactly = 1) { demoRepository.findById(demoId) }
    }

    @Test
    fun `should throw EntityNotFoundException when demo not found`() {
        // given
        val demoId = UUID.randomUUID()
        every { demoRepository.findById(demoId) } returns null

        // when & then
        val exception = assertThrows<EntityNotFoundException> {
            demoService.getById(demoId)
        }
        assertTrue(exception.message!!.contains("Demo"))
        assertTrue(exception.message!!.contains(demoId.toString()))
        verify(exactly = 1) { demoRepository.findById(demoId) }
    }

    @Test
    fun `should get all demos successfully`() {
        // given
        val demos = listOf(
            createTestDemo(UUID.randomUUID(), "Demo 1"),
            createTestDemo(UUID.randomUUID(), "Demo 2")
        )
        every { demoRepository.findAll() } returns demos

        // when
        val result = demoService.getAll()

        // then
        assertEquals(2, result.size)
        assertEquals(demos, result)
        verify(exactly = 1) { demoRepository.findAll() }
    }

    @Test
    fun `should get all active demos successfully`() {
        // given
        val activeDemos = listOf(
            createTestDemo(UUID.randomUUID(), "Active Demo 1"),
            createTestDemo(UUID.randomUUID(), "Active Demo 2")
        )
        every { demoRepository.findAllActive() } returns activeDemos

        // when
        val result = demoService.getAllActive()

        // then
        assertEquals(2, result.size)
        assertEquals(activeDemos, result)
        verify(exactly = 1) { demoRepository.findAllActive() }
    }

    @Test
    fun `should update demo successfully`() {
        // given
        val demoId = UUID.randomUUID()
        val existingDemo = createTestDemo(demoId, "Old Description")
        val command = UpdateDemoUseCase.UpdateDemoCommand(
            id = demoId,
            description = "Updated Description",
            longDescription = "Updated long description",
            demoPath = "/demos/updated.pdf",
            isActive = false
        )

        every { demoRepository.findById(demoId) } returns existingDemo
        every { demoRepository.save(any()) } answers { firstArg() }

        // when
        val result = demoService.update(command)

        // then
        assertEquals("Updated Description", result.description)
        assertEquals("Updated long description", result.longDescription)
        assertEquals("/demos/updated.pdf", result.demoPath)
        assertFalse(result.isActive)
        verify(exactly = 1) { demoRepository.findById(demoId) }
        verify(exactly = 1) { demoRepository.save(any()) }
    }

    @Test
    fun `should throw EntityNotFoundException when updating non-existent demo`() {
        // given
        val demoId = UUID.randomUUID()
        val command = UpdateDemoUseCase.UpdateDemoCommand(
            id = demoId,
            description = "Updated",
            longDescription = "Updated long",
            demoPath = null,
            isActive = true
        )

        every { demoRepository.findById(demoId) } returns null

        // when & then
        assertThrows<EntityNotFoundException> {
            demoService.update(command)
        }
        verify(exactly = 1) { demoRepository.findById(demoId) }
        verify(exactly = 0) { demoRepository.save(any()) }
    }

    @Test
    fun `should delete demo successfully`() {
        // given
        val demoId = UUID.randomUUID()
        every { demoRepository.existsById(demoId) } returns true
        every { demoRepository.deleteById(demoId) } just Runs

        // when
        demoService.delete(demoId)

        // then
        verify(exactly = 1) { demoRepository.existsById(demoId) }
        verify(exactly = 1) { demoRepository.deleteById(demoId) }
    }

    @Test
    fun `should throw EntityNotFoundException when deleting non-existent demo`() {
        // given
        val demoId = UUID.randomUUID()
        every { demoRepository.existsById(demoId) } returns false

        // when & then
        assertThrows<EntityNotFoundException> {
            demoService.delete(demoId)
        }
        verify(exactly = 1) { demoRepository.existsById(demoId) }
        verify(exactly = 0) { demoRepository.deleteById(any()) }
    }

    // Helper method
    private fun createTestDemo(id: UUID, description: String): Demo {
        val now = LocalDateTime.now()
        return Demo(
            id = id,
            description = description,
            longDescription = "Long description for $description",
            demoPath = "/demos/test.pdf",
            isActive = true,
            createdAt = now,
            updatedAt = now
        )
    }
}
