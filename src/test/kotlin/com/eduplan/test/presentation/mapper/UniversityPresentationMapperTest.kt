package com.eduplan.test.presentation.mapper

import com.eduplan.domain.model.University
import com.eduplan.presentation.dto.CreateUniversityRequestDto
import com.eduplan.presentation.dto.UpdateUniversityRequestDto
import com.eduplan.presentation.mapper.UniversityPresentationMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class UniversityPresentationMapperTest {
    private val mapper = UniversityPresentationMapper()

    @Test
    fun `toResponseDto should map University to UniversityResponseDto correctly`() {
        val university = University(
            id = UUID.randomUUID(),
            name = "Московский Государственный Университет",
            address = "Ленинские горы, 1, Москва",
            country = "Россия",
            website = "www.msu.ru",
            createdAt = LocalDateTime.of(2023, 1, 1, 0, 0),
            deletedAt = null
        )

        val result = mapper.toResponseDto(university)

        assertThat(result.id).isEqualTo(university.id)
        assertThat(result.name).isEqualTo("Московский Государственный Университет")
        assertThat(result.address).isEqualTo("Ленинские горы, 1, Москва")
        assertThat(result.country).isEqualTo("Россия")
        assertThat(result.website).isEqualTo("www.msu.ru")
        assertThat(result.createdAt).isEqualTo(university.createdAt)
        assertThat(result.deletedAt).isEqualTo(university.deletedAt)
    }

    @Test
    fun `toCreateCommand should map CreateUniversityRequestDto to CreateUniversityCommand correctly`() {
        val dto = CreateUniversityRequestDto(
            name = "Санкт-Петербургский Государственный Университет",
            address = "Университетская наб., 7-9, Санкт-Петербург",
            country = "Россия",
            website = "www.spbu.ru"
        )

        val result = mapper.toCreateCommand(dto)

        assertThat(result.name).isEqualTo("Санкт-Петербургский Государственный Университет")
        assertThat(result.address).isEqualTo("Университетская наб., 7-9, Санкт-Петербург")
        assertThat(result.country).isEqualTo("Россия")
        assertThat(result.website).isEqualTo("www.spbu.ru")
    }

    @Test
    fun `toUpdateCommand should map UpdateUniversityRequestDto to UpdateUniversityCommand correctly`() {
        val dto = UpdateUniversityRequestDto(
            name = "Обновленный Университет",
            address = "Новый адрес, 123",
            country = "Россия",
            website = "www.updated.com"
        )

        val result = mapper.toUpdateCommand(dto)

        assertThat(result.name).isEqualTo("Обновленный Университет")
        assertThat(result.address).isEqualTo("Новый адрес, 123")
        assertThat(result.country).isEqualTo("Россия")
        assertThat(result.website).isEqualTo("www.updated.com")
    }

    @Test
    fun `toResponseDto should handle deleted university correctly`() {
        val deletedAt = LocalDateTime.of(2023, 12, 20, 12, 0)
        val university = University(
            id = UUID.randomUUID(),
            name = "Удаленный университет",
            address = "Старый адрес",
            country = "Россия",
            website = "www.deleted.com",
            createdAt = LocalDateTime.of(2023, 1, 1, 0, 0),
            deletedAt = deletedAt
        )

        val result = mapper.toResponseDto(university)

        assertThat(result.deletedAt).isEqualTo(deletedAt)
        assertThat(result.deletedAt).isNotNull()
    }

    @Test
    fun `toResponseDto should handle university with minimal fields`() {
        val university = University(
            id = UUID.randomUUID(),
            name = "МИФИ",
            address = "",
            country = "Россия",
            website = "",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        val result = mapper.toResponseDto(university)

        assertThat(result.name).isEqualTo("МИФИ")
        assertThat(result.address).isEmpty()
        assertThat(result.country).isEqualTo("Россия")
        assertThat(result.website).isEmpty()
        assertThat(result.deletedAt).isNull()
    }

    @Test
    fun `toCreateCommand should handle empty strings correctly`() {
        val dto = CreateUniversityRequestDto(
            name = "",
            address = "",
            country = "",
            website = ""
        )

        val result = mapper.toCreateCommand(dto)

        assertThat(result.name).isEmpty()
        assertThat(result.address).isEmpty()
        assertThat(result.country).isEmpty()
        assertThat(result.website).isEmpty()
    }

    @Test
    fun `toUpdateCommand should handle empty strings correctly`() {
        val dto = UpdateUniversityRequestDto(
            name = "",
            address = "",
            country = "",
            website = ""
        )

        val result = mapper.toUpdateCommand(dto)

        assertThat(result.name).isEmpty()
        assertThat(result.address).isEmpty()
        assertThat(result.country).isEmpty()
        assertThat(result.website).isEmpty()
    }

    @Test
    fun `toResponseDto should handle website without protocol correctly`() {
        val university = University(
            id = UUID.randomUUID(),
            name = "ТГУ",
            address = "пр. Ленина, 36, Томск",
            country = "Россия",
            website = "www.tsu.ru",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        val result = mapper.toResponseDto(university)

        assertThat(result.website).isEqualTo("www.tsu.ru")
        assertThat(result.website).doesNotContain("http://")
        assertThat(result.website).doesNotContain("https://")
    }

    @Test
    fun `toResponseDto should handle different countries correctly`() {
        val russianUniversity = University(
            id = UUID.randomUUID(),
            name = "КФУ",
            address = "ул. Кремлевская, 18, Казань",
            country = "Россия",
            website = "www.kpfu.ru",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        val usUniversity = University(
            id = UUID.randomUUID(),
            name = "Harvard University",
            address = "Cambridge, MA 02138",
            country = "USA",
            website = "www.harvard.edu",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        val russianResult = mapper.toResponseDto(russianUniversity)
        val usResult = mapper.toResponseDto(usUniversity)

        assertThat(russianResult.country).isEqualTo("Россия")
        assertThat(usResult.country).isEqualTo("USA")
        assertThat(russianResult).isNotEqualTo(usResult)
    }

    @Test
    fun `toCreateCommand should preserve all fields from DTO`() {
        val dto = CreateUniversityRequestDto(
            name = "НГУ",
            address = "ул. Пирогова, 1, Новосибирск",
            country = "Россия",
            website = "www.nsu.ru"
        )

        val result = mapper.toCreateCommand(dto)

        assertThat(result.name).isEqualTo(dto.name)
        assertThat(result.address).isEqualTo(dto.address)
        assertThat(result.country).isEqualTo(dto.country)
        assertThat(result.website).isEqualTo(dto.website)
    }

    @Test
    fun `toUpdateCommand should preserve all fields from DTO`() {
        val dto = UpdateUniversityRequestDto(
            name = "Обновленное название",
            address = "Обновленный адрес",
            country = "Обновленная страна",
            website = "www.updated.com"
        )

        val result = mapper.toUpdateCommand(dto)

        assertThat(result.name).isEqualTo(dto.name)
        assertThat(result.address).isEqualTo(dto.address)
        assertThat(result.country).isEqualTo(dto.country)
        assertThat(result.website).isEqualTo(dto.website)
    }

    @Test
    fun `toResponseDto should handle long names and addresses correctly`() {
        val university = University(
            id = UUID.randomUUID(),
            name = "Московский Государственный Университет имени М.В. Ломоносова",
            address = "Ленинские горы, д. 1, стр. 51, Москва, Россия, 119991",
            country = "Россия",
            website = "www.msu.ru",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        val result = mapper.toResponseDto(university)

        assertThat(result.name).hasSizeGreaterThan(50)
        assertThat(result.address).hasSizeGreaterThan(50)
        assertThat(result.name).contains("Ломоносова")
        assertThat(result.address).contains("119991")
    }
}