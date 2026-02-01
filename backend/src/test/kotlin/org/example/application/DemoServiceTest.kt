package org.example.application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.application.service.DemoService
import org.example.application.port.output.DemoRepositoryPort
import org.example.domain.Demo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DemoServiceTest {
    private val repository = mockk<DemoRepositoryPort>(relaxed = true)
    private val service = DemoService(repository)

    @Test
    fun `create should call repository and return saved demo`() {
        val name = "test"
        val saved = Demo(id = 1, name = name)
        every { repository.save(any()) } returns saved

        val result = service.create(name)

        assertEquals(saved, result)
        verify { repository.save(match { it.name == name }) }
    }

    @Test
    fun `get should delegate to repository`() {
        val demo = Demo(id = 2, name = "ok")
        every { repository.findById(2) } returns demo

        val result = service.get(2)

        assertEquals(demo, result)
    }
}
