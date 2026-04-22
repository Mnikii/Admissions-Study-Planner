package com.eduplan.test.presentation.controller

import com.eduplan.application.port.input.UniversityUseCase
import com.eduplan.domain.model.University
import com.eduplan.presentation.controller.UniversityController
import com.eduplan.presentation.dto.CreateUniversityRequestDto
import com.eduplan.presentation.dto.UpdateUniversityRequestDto
import com.eduplan.presentation.mapper.UniversityPresentationMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.util.*

class UniversityControllerTest {
    private lateinit var universityUseCase: UniversityUseCase
    private lateinit var mapper: UniversityPresentationMapper
    private lateinit var controller: UniversityController

    private val testUniversity = University(
        id = UUID.randomUUID(),
        name = "Московский Государственный Университет",
        address = "Ленинские горы, 1, Москва",
        country = "Россия",
        website = "www.msu.ru",
        createdAt = LocalDateTime.of(2024, 1, 1, 10, 0),
        deletedAt = null
    )

    @BeforeEach
    fun setUp() {
        universityUseCase = mockk()
        mapper = UniversityPresentationMapper()
        controller = UniversityController(universityUseCase, mapper)
    }

    @Test
    fun `createUniversity should create and return university`() {
        val request = CreateUniversityRequestDto(
            name = "Московский Государственный Университет",
            address = "Ленинские горы, 1, Москва",
            country = "Россия",
            website = "www.msu.ru"
        )

        every { universityUseCase.createUniversity(mapper.toCreateCommand(request)) } returns testUniversity

        val response = controller.createUniversity(request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.name).isEqualTo("Московский Государственный Университет")
        assertThat(response.body?.address).isEqualTo("Ленинские горы, 1, Москва")
        assertThat(response.body?.country).isEqualTo("Россия")
        assertThat(response.body?.website).isEqualTo("www.msu.ru")
        verify { universityUseCase.createUniversity(mapper.toCreateCommand(request)) }
    }

    @Test
    fun `getUniversityById should return university when found`() {
        every { universityUseCase.getUniversityById(testUniversity.id) } returns testUniversity

        val response = controller.getUniversityById(testUniversity.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.id).isEqualTo(testUniversity.id)
        assertThat(response.body?.name).isEqualTo("Московский Государственный Университет")
        assertThat(response.body?.address).isEqualTo("Ленинские горы, 1, Москва")
        assertThat(response.body?.country).isEqualTo("Россия")
        assertThat(response.body?.website).isEqualTo("www.msu.ru")
    }

    @Test
    fun `getUniversityById should return 404 when university not found`() {
        val universityId = UUID.randomUUID()
        every { universityUseCase.getUniversityById(universityId) } returns null

        val response = controller.getUniversityById(universityId)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isNull()
    }

    @Test
    fun `getAllUniversities should return list of universities`() {
        every { universityUseCase.getAllUniversities() } returns listOf(testUniversity)

        val response = controller.getAllUniversities()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).hasSize(1)
        assertThat(response.body?.first()?.name).isEqualTo("Московский Государственный Университет")
        assertThat(response.body?.first()?.country).isEqualTo("Россия")
    }

    @Test
    fun `getAllUniversities should return empty list when no universities`() {
        every { universityUseCase.getAllUniversities() } returns emptyList()

        val response = controller.getAllUniversities()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEmpty()
    }

    @Test
    fun `getUniversityByName should return university when found`() {
        val universityName = "МГУ"
        every { universityUseCase.getByName(universityName) } returns testUniversity

        val response = controller.getUniversityByName(universityName)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.name).isEqualTo("Московский Государственный Университет")
        assertThat(response.body?.id).isEqualTo(testUniversity.id)
    }

    @Test
    fun `getUniversityByName should return 404 when university not found`() {
        val universityName = "Несуществующий университет"
        every { universityUseCase.getByName(universityName) } returns null

        val response = controller.getUniversityByName(universityName)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isNull()
    }

    @Test
    fun `updateUniversity should update and return university`() {
        val request = UpdateUniversityRequestDto(
            name = "МГУ им. Ломоносова",
            address = "Ленинские горы, 1, Москва",
            country = "Россия",
            website = "www.msu.ru"
        )
        val command = mapper.toUpdateCommand(request)
        val updatedUniversity = testUniversity.copy(name = "МГУ им. Ломоносова")

        every { universityUseCase.updateUniversity(testUniversity.id, command) } returns updatedUniversity

        val response = controller.updateUniversity(testUniversity.id, request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.name).isEqualTo("МГУ им. Ломоносова")
        assertThat(response.body?.address).isEqualTo("Ленинские горы, 1, Москва")
        assertThat(response.body?.country).isEqualTo("Россия")
        assertThat(response.body?.website).isEqualTo("www.msu.ru")
    }

    @Test
    fun `updateUniversity should return 404 when university not found`() {
        val universityId = UUID.randomUUID()
        val request = UpdateUniversityRequestDto(
            name = "Обновленный университет",
            address = "Новый адрес",
            country = "Россия",
            website = "www.updated.com"
        )
        val command = mapper.toUpdateCommand(request)

        every { universityUseCase.updateUniversity(universityId, command) } throws NoSuchElementException("University not found")

        val response = controller.updateUniversity(universityId, request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `deleteUniversity should return no content on success`() {
        every { universityUseCase.deleteUniversity(testUniversity.id) } returns Unit

        val response = controller.deleteUniversity(testUniversity.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `deleteUniversity should return 404 when university not found`() {
        val universityId = UUID.randomUUID()
        every { universityUseCase.deleteUniversity(universityId) } throws NoSuchElementException("University not found")

        val response = controller.deleteUniversity(universityId)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `deleteUniversity should return 404 when exception occurs`() {
        val universityId = UUID.randomUUID()
        every { universityUseCase.deleteUniversity(universityId) } throws Exception("Database error")

        val response = controller.deleteUniversity(universityId)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `createUniversity should handle validation errors gracefully`() {
        val request = CreateUniversityRequestDto(
            name = "",
            address = "",
            country = "",
            website = ""
        )

        // Assuming mapper creates command even with empty strings
        val emptyUniversity = University(
            id = UUID.randomUUID(),
            name = "",
            address = "",
            country = "",
            website = "",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        every { universityUseCase.createUniversity(mapper.toCreateCommand(request)) } returns emptyUniversity

        val response = controller.createUniversity(request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.name).isEmpty()
        assertThat(response.body?.address).isEmpty()
    }
}