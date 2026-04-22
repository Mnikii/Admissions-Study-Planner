package com.eduplan.test.application.service

import com.eduplan.application.port.input.UniversityUseCase
import com.eduplan.application.service.UniversityService
import com.eduplan.domain.model.University
import com.eduplan.infrastructure.adapter.UniversityRepositoryAdapter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class UniversityServiceTest {
    private lateinit var universityRepository: UniversityRepositoryAdapter
    private lateinit var universityService: UniversityService

    @BeforeEach
    fun setUp() {
        universityRepository = mockk()
        universityService = UniversityService(universityRepository)
    }

    @Test
    fun `createUniversity should create and save new university`() {
        val command = UniversityUseCase.CreateUniversityCommand(
            name = "Московский Государственный Университет",
            address = "Ленинские горы, 1, Москва",
            country = "Россия",
            website = "www.msu.ru"
        )

        val expectedUniversity = University(
            id = UUID.randomUUID(),
            name = command.name,
            address = command.address,
            country = command.country,
            website = command.website,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        every { universityRepository.save(any<University>()) } returns expectedUniversity

        val result = universityService.createUniversity(command)

        assertThat(result.name).isEqualTo("Московский Государственный Университет")
        assertThat(result.address).isEqualTo("Ленинские горы, 1, Москва")
        assertThat(result.country).isEqualTo("Россия")
        assertThat(result.website).isEqualTo("www.msu.ru")
        assertThat(result.createdAt).isNotNull()
        assertThat(result.deletedAt).isNull()

        verify(exactly = 1) { universityRepository.save(any<University>()) }
    }

    @Test
    fun `getUniversityById should return university when found`() {
        val university = testUniversity(name = "СПбГУ")

        every { universityRepository.findById(university.id) } returns university

        val result = universityService.getUniversityById(university.id)

        assertThat(result).isEqualTo(university)
        verify(exactly = 1) { universityRepository.findById(university.id) }
    }

    @Test
    fun `getUniversityById should return null when university not found`() {
        val id = UUID.randomUUID()

        every { universityRepository.findById(id) } returns null

        val result = universityService.getUniversityById(id)

        assertThat(result).isNull()
        verify(exactly = 1) { universityRepository.findById(id) }
    }

    @Test
    fun `updateUniversity should update and save university`() {
        val existing = testUniversity(
            name = "Old Name",
            address = "Old Address",
            country = "Россия",
            website = "old.com"
        )

        val command = UniversityUseCase.UpdateUniversityCommand(
            name = "New University Name",
            address = "New Address",
            country = "Россия",
            website = "new.com"
        )

        val updatedUniversity = existing.copy(
            name = command.name,
            address = command.address,
            country = command.country,
            website = command.website
        )

        every { universityRepository.findById(existing.id) } returns existing
        every { universityRepository.save(any<University>()) } returns updatedUniversity

        val result = universityService.updateUniversity(existing.id, command)

        assertThat(result.name).isEqualTo("New University Name")
        assertThat(result.address).isEqualTo("New Address")
        assertThat(result.country).isEqualTo("Россия")
        assertThat(result.website).isEqualTo("new.com")
        assertThat(result.createdAt).isEqualTo(existing.createdAt)

        verify(exactly = 1) { universityRepository.findById(existing.id) }
        verify(exactly = 1) { universityRepository.save(any<University>()) }
    }

    @Test
    fun `updateUniversity should throw exception when university not found`() {
        val id = UUID.randomUUID()
        val command = UniversityUseCase.UpdateUniversityCommand(
            name = "New Name",
            address = "New Address",
            country = "Россия",
            website = "new.com"
        )

        every { universityRepository.findById(id) } returns null

        assertThrows<Exception> {
            universityService.updateUniversity(id, command)
        }

        verify(exactly = 1) { universityRepository.findById(id) }
        verify(exactly = 0) { universityRepository.save(any()) }
    }

    @Test
    fun `deleteUniversity should soft delete active university`() {
        val existing = testUniversity()

        val deletedUniversity = existing.copy(deletedAt = LocalDateTime.now())

        every { universityRepository.findById(existing.id) } returns existing
        every { universityRepository.save(any<University>()) } returns deletedUniversity

        universityService.deleteUniversity(existing.id)

        verify(exactly = 1) { universityRepository.findById(existing.id) }
        verify(exactly = 1) { universityRepository.save(match { university ->
            university.id == existing.id && university.deletedAt != null
        }) }
    }

    @Test
    fun `deleteUniversity should throw exception when university not found`() {
        val id = UUID.randomUUID()

        every { universityRepository.findById(id) } returns null

        assertThrows<Exception> {
            universityService.deleteUniversity(id)
        }

        verify(exactly = 1) { universityRepository.findById(id) }
        verify(exactly = 0) { universityRepository.save(any()) }
    }

    @Test
    fun `deleteUniversity should not delete already deleted university`() {
        val existing = testUniversity(deleted = true)

        every { universityRepository.findById(existing.id) } returns existing

        universityService.deleteUniversity(existing.id)

        verify(exactly = 1) { universityRepository.findById(existing.id) }
        verify(exactly = 0) { universityRepository.save(any()) }
    }

    @Test
    fun `getAllUniversities should return all universities`() {
        val universities = listOf(
            testUniversity(name = "University 1"),
            testUniversity(name = "University 2")
        )

        every { universityRepository.findAll() } returns universities

        val result = universityService.getAllUniversities()

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("University 1")
        assertThat(result[1].name).isEqualTo("University 2")

        verify(exactly = 1) { universityRepository.findAll() }
    }

    @Test
    fun `getAllUniversities should return empty list when no universities`() {
        every { universityRepository.findAll() } returns emptyList()

        val result = universityService.getAllUniversities()

        assertThat(result).isEmpty()
        verify(exactly = 1) { universityRepository.findAll() }
    }

    @Test
    fun `getByName should return university when found`() {
        val university = testUniversity(name = "МГУ")

        every { universityRepository.findByName("МГУ") } returns university

        val result = universityService.getByName("МГУ")

        assertThat(result).isEqualTo(university)
        verify(exactly = 1) { universityRepository.findByName("МГУ") }
    }

    @Test
    fun `getByName should return null when university not found`() {
        every { universityRepository.findByName("Несуществующий университет") } returns null

        val result = universityService.getByName("Несуществующий университет")

        assertThat(result).isNull()
        verify(exactly = 1) { universityRepository.findByName("Несуществующий университет") }
    }

    @Test
    fun `createUniversity should generate unique id for each university`() {
        val command1 = UniversityUseCase.CreateUniversityCommand(
            name = "Университет 1",
            address = "Адрес 1",
            country = "Россия",
            website = "www.uni1.com"
        )

        val command2 = UniversityUseCase.CreateUniversityCommand(
            name = "Университет 2",
            address = "Адрес 2",
            country = "Россия",
            website = "www.uni2.com"
        )

        val university1 = University(
            id = UUID.randomUUID(),
            name = command1.name,
            address = command1.address,
            country = command1.country,
            website = command1.website,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        val university2 = University(
            id = UUID.randomUUID(),
            name = command2.name,
            address = command2.address,
            country = command2.country,
            website = command2.website,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        every { universityRepository.save(any<University>()) } returnsMany listOf(university1, university2)

        val result1 = universityService.createUniversity(command1)
        val result2 = universityService.createUniversity(command2)

        assertThat(result1.id).isNotEqualTo(result2.id)
        verify(exactly = 2) { universityRepository.save(any<University>()) }
    }

    private fun testUniversity(
        name: String = "Test University",
        address: String = "Test Address, 123",
        country: String = "Россия",
        website: String = "www.testuniversity.com",
        deleted: Boolean = false,
    ): University = University(
        id = UUID.randomUUID(),
        name = name,
        address = address,
        country = country,
        website = website,
        createdAt = LocalDateTime.now(),
        deletedAt = if (deleted) LocalDateTime.now() else null
    )
}