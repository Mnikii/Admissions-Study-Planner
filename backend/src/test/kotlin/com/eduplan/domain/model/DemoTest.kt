package com.eduplan.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID

/**
 * Unit тесты для доменной модели Demo
 *
 * Проверяем бизнес-правила и валидацию без зависимостей от фреймворков.
 */
class DemoTest {

    @Test
    fun `should create demo with valid data`() {
        // given
        val id = UUID.randomUUID()
        val description = "Test Demo"
        val longDescription = "This is a detailed description"
        val now = LocalDateTime.now()

        // when
        val demo = Demo(
            id = id,
            description = description,
            longDescription = longDescription,
            demoPath = "/demos/test.pdf",
            isActive = true,
            createdAt = now,
            updatedAt = now
        )

        // then
        assertEquals(id, demo.id)
        assertEquals(description, demo.description)
        assertEquals(longDescription, demo.longDescription)
        assertEquals("/demos/test.pdf", demo.demoPath)
        assertTrue(demo.isActive)
    }

    @Test
    fun `should fail when description is blank`() {
        // given
        val id = UUID.randomUUID()
        val now = LocalDateTime.now()

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            Demo(
                id = id,
                description = "   ", // blank
                longDescription = "Valid description",
                demoPath = null,
                isActive = true,
                createdAt = now,
                updatedAt = now
            )
        }
        assertTrue(exception.message!!.contains("Description cannot be blank"))
    }

    @Test
    fun `should fail when description exceeds max length`() {
        // given
        val id = UUID.randomUUID()
        val now = LocalDateTime.now()
        val tooLongDescription = "a".repeat(256)

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            Demo(
                id = id,
                description = tooLongDescription,
                longDescription = "Valid description",
                demoPath = null,
                isActive = true,
                createdAt = now,
                updatedAt = now
            )
        }
        assertTrue(exception.message!!.contains("Description cannot exceed 255 characters"))
    }

    @Test
    fun `should fail when long description is blank`() {
        // given
        val id = UUID.randomUUID()
        val now = LocalDateTime.now()

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            Demo(
                id = id,
                description = "Valid",
                longDescription = "  ", // blank
                demoPath = null,
                isActive = true,
                createdAt = now,
                updatedAt = now
            )
        }
        assertTrue(exception.message!!.contains("Long description cannot be blank"))
    }

    @Test
    fun `should update description successfully`() {
        // given
        val demo = createValidDemo()

        // when
        val updated = demo.updateDescription("New description", "New long description")

        // then
        assertEquals("New description", updated.description)
        assertEquals("New long description", updated.longDescription)
        assertTrue(updated.updatedAt.isAfter(demo.createdAt))
    }

    @Test
    fun `should deactivate demo`() {
        // given
        val demo = createValidDemo()
        assertTrue(demo.isActive)

        // when
        val deactivated = demo.deactivate()

        // then
        assertFalse(deactivated.isActive)
        assertTrue(deactivated.updatedAt.isAfter(demo.updatedAt))
    }

    @Test
    fun `should activate demo`() {
        // given
        val demo = createValidDemo().deactivate()
        assertFalse(demo.isActive)

        // when
        val activated = demo.activate()

        // then
        assertTrue(activated.isActive)
        assertTrue(activated.updatedAt.isAfter(demo.updatedAt))
    }

    @Test
    fun `should allow null demo path`() {
        // given & when
        val demo = createValidDemo().copy(demoPath = null)

        // then
        assertNull(demo.demoPath)
    }

    @Test
    fun `should fail when demo path is blank if provided`() {
        // given
        val id = UUID.randomUUID()
        val now = LocalDateTime.now()

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            Demo(
                id = id,
                description = "Valid",
                longDescription = "Valid long",
                demoPath = "   ", // blank but provided
                isActive = true,
                createdAt = now,
                updatedAt = now
            )
        }
        assertTrue(exception.message!!.contains("Demo path cannot be blank if provided"))
    }

    // Helper method
    private fun createValidDemo(): Demo {
        val now = LocalDateTime.now()
        return Demo(
            id = UUID.randomUUID(),
            description = "Test Demo",
            longDescription = "This is a test demo with detailed description",
            demoPath = "/demos/test.pdf",
            isActive = true,
            createdAt = now,
            updatedAt = now
        )
    }
}
