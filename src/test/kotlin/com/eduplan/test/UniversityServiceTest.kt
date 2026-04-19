package com.eduplan.test

import com.eduplan.application.port.input.UniversityUseCase
import com.eduplan.application.service.UniversityService
import com.eduplan.domain.model.University
import com.eduplan.infrastructure.adapter.UniversityRepositoryAdapter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

class UniversityServiceTest {

    private val repo = mockk<UniversityRepositoryAdapter>()


    private val universityService = UniversityService(
        universityRepository = repo
    )



    @Test
    fun `getByName - должен вернуть университет когда он существует`() {
        val expectedUniversity = University(
            id = UUID.randomUUID(),
            address = "Уличная улица, 6",
            country = "Россия",
            createdAt = LocalDateTime.of(2024, 12, 9, 23, 10),
            deletedAt = null,
            name = "Университет университетов",
            website = "www.university.com"
        )

        every { repo.findByName("Университет университетов") } returns expectedUniversity

        val result = universityService.getByName("Университет университетов")

        assertEquals(expectedUniversity, result)
        verify(exactly = 1) { repo.findByName("Университет университетов") }
    }

    @Test
    fun `getByName - должен вернуть null когда университет не найден`() {
        every { repo.findByName("Несуществующий университет") } returns null

        val result = universityService.getByName("Несуществующий университет")

        assertNull(result)
        verify(exactly = 1) { repo.findByName("Несуществующий университет") }
    }

    @Test
    fun `createUniversity - должен создать и сохранить университет`() {
        val command = UniversityUseCase.CreateUniversityCommand(
            name = "Новый университет",
            address = "Новая улица, 1",
            country = "Россия",
            website = "www.newuniversity.com"
        )

        every { repo.save(any<University>()) } answers { firstArg() }

        val result = universityService.createUniversity(command)

        assertEquals(command.name, result.name)
        assertEquals(command.address, result.address)
        assertEquals(command.country, result.country)
        assertEquals(command.website, result.website)
        assertNotNull(result.id)
        assertNotNull(result.createdAt)
        assertNull(result.deletedAt)

        verify(exactly = 1) { repo.save(any<University>()) }
    }

    @Test
    fun `getUniversityById - должен вернуть университет по ID`() {
        val id = UUID.randomUUID()
        val expectedUniversity = University(
            id = id,
            name = "Тестовый университет",
            address = "Тестовая улица",
            country = "Россия",
            website = "www.test.com",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        every { repo.findById(id) } returns expectedUniversity

        val result = universityService.getUniversityById(id)

        assertEquals(expectedUniversity, result)
        verify(exactly = 1) { repo.findById(id) }
    }

    @Test
    fun `getUniversityById - должен вернуть null когда университет не найден`() {
        val id = UUID.randomUUID()
        every { repo.findById(id) } returns null

        val result = universityService.getUniversityById(id)

        assertNull(result)
        verify(exactly = 1) { repo.findById(id) }
    }

    @Test
    fun `updateUniversity - должен обновить существующий университет`() {
        val id = UUID.randomUUID()
        val existingUniversity = University(
            id = id,
            name = "Старое название",
            address = "Старый адрес",
            country = "Россия",
            website = "www.old.com",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        val updateCommand = UniversityUseCase.UpdateUniversityCommand(
            name = "Новое название",
            address = "Новый адрес",
            country = "Россия",
            website = "www.new.com"
        )

        every { repo.findById(id) } returns existingUniversity
        every { repo.save(any<University>()) } answers { firstArg() }

        val result = universityService.updateUniversity(id, updateCommand)

        assertEquals(updateCommand.name, result.name)
        assertEquals(updateCommand.address, result.address)
        assertEquals(updateCommand.country, result.country)
        assertEquals(updateCommand.website, result.website)
        assertEquals(existingUniversity.id, result.id)
        assertEquals(existingUniversity.createdAt, result.createdAt)

        verify(exactly = 1) { repo.findById(id) }
        verify(exactly = 1) { repo.save(any<University>()) }
    }

    @Test
    fun `updateUniversity - должен выбросить исключение когда университет не найден`() {
        val id = UUID.randomUUID()
        val updateCommand = UniversityUseCase.UpdateUniversityCommand(
            name = "Новое название",
            address = "Новый адрес",
            country = "Россия",
            website = "www.new.com"
        )

        every { repo.findById(id) } returns null

        val exception = assertThrows<Exception> {
            universityService.updateUniversity(id, updateCommand)
        }

        assertTrue(exception.message?.contains("University with ID $id not found") == true)
        verify(exactly = 1) { repo.findById(id) }
        verify(exactly = 0) { repo.save(any()) }
    }

    @Test
    fun `deleteUniversity - должен мягко удалить университет`() {
        val id = UUID.randomUUID()
        val existingUniversity = University(
            id = id,
            name = "Университет",
            address = "Адрес",
            country = "Россия",
            website = "www.test.com",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        every { repo.findById(id) } returns existingUniversity
        every { repo.save(any<University>()) } answers { firstArg() }

        universityService.deleteUniversity(id)

        verify(exactly = 1) { repo.findById(id) }
        verify(exactly = 1) { repo.save(match { university ->
            university.id == id && university.deletedAt != null
        }) }
    }

    @Test
    fun `deleteUniversity - должен выбросить исключение когда университет не найден`() {
        val id = UUID.randomUUID()
        every { repo.findById(id) } returns null

        val exception = assertThrows<Exception> {
            universityService.deleteUniversity(id)
        }

        assertTrue(exception.message?.contains("University with ID $id not found") == true)
        verify(exactly = 1) { repo.findById(id) }
        verify(exactly = 0) { repo.save(any()) }
    }

    @Test
    fun `getAllUniversities - должен вернуть список всех университетов`() {
        val universities = listOf(
            University(
                id = UUID.randomUUID(),
                name = "Университет 1",
                address = "Адрес 1",
                country = "Россия",
                website = "www.uni1.com",
                createdAt = LocalDateTime.now(),
                deletedAt = null
            ),
            University(
                id = UUID.randomUUID(),
                name = "Университет 2",
                address = "Адрес 2",
                country = "Россия",
                website = "www.uni2.com",
                createdAt = LocalDateTime.now(),
                deletedAt = null
            )
        )

        every { repo.findAll() } returns universities

        val result = universityService.getAllUniversities()

        assertEquals(2, result.size)
        assertEquals(universities, result)
        verify(exactly = 1) { repo.findAll() }
    }

    @Test
    fun `getAllUniversities - должен вернуть пустой список когда университетов нет`() {
        every { repo.findAll() } returns emptyList()

        val result = universityService.getAllUniversities()

        assertTrue(result.isEmpty())
        verify(exactly = 1) { repo.findAll() }
    }
}