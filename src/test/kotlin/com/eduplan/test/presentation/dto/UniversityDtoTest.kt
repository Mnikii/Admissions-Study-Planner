package com.eduplan.presentation.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class UniversityDtoTest {

    @Test
    fun `UniversityResponseDto should be created correctly`() {
        val id = UUID.randomUUID()
        val createdAt = LocalDateTime.of(2023, 1, 1, 0, 0)

        val dto = UniversityResponseDto(
            id = id,
            name = "Московский Государственный Университет",
            address = "Ленинские горы, 1, Москва",
            country = "Россия",
            website = "www.msu.ru",
            createdAt = createdAt,
            deletedAt = null
        )

        assertThat(dto.id).isEqualTo(id)
        assertThat(dto.name).isEqualTo("Московский Государственный Университет")
        assertThat(dto.address).isEqualTo("Ленинские горы, 1, Москва")
        assertThat(dto.country).isEqualTo("Россия")
        assertThat(dto.website).isEqualTo("www.msu.ru")
        assertThat(dto.createdAt).isEqualTo(createdAt)
        assertThat(dto.deletedAt).isNull()
    }

    @Test
    fun `CreateUniversityRequestDto should be created correctly`() {
        val dto = CreateUniversityRequestDto(
            name = "Санкт-Петербургский Государственный Университет",
            address = "Университетская наб., 7-9, Санкт-Петербург",
            country = "Россия",
            website = "www.spbu.ru"
        )

        assertThat(dto.name).isEqualTo("Санкт-Петербургский Государственный Университет")
        assertThat(dto.address).isEqualTo("Университетская наб., 7-9, Санкт-Петербург")
        assertThat(dto.country).isEqualTo("Россия")
        assertThat(dto.website).isEqualTo("www.spbu.ru")
    }

    @Test
    fun `UpdateUniversityRequestDto should be created correctly`() {
        val dto = UpdateUniversityRequestDto(
            name = "Обновленный Университет",
            address = "Новый адрес, 123",
            country = "Россия",
            website = "www.updated.com"
        )

        assertThat(dto.name).isEqualTo("Обновленный Университет")
        assertThat(dto.address).isEqualTo("Новый адрес, 123")
        assertThat(dto.country).isEqualTo("Россия")
        assertThat(dto.website).isEqualTo("www.updated.com")
    }

    @Test
    fun `UniversityResponseDto should handle null deletedAt correctly`() {
        val dto = UniversityResponseDto(
            id = UUID.randomUUID(),
            name = "НГУ",
            address = "ул. Пирогова, 1, Новосибирск",
            country = "Россия",
            website = "www.nsu.ru",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        assertThat(dto.deletedAt).isNull()
    }

    @Test
    fun `UniversityResponseDto should handle deletedAt with value correctly`() {
        val deletedAt = LocalDateTime.of(2024, 12, 9, 23, 10)

        val dto = UniversityResponseDto(
            id = UUID.randomUUID(),
            name = "Удаленный университет",
            address = "Старый адрес",
            country = "Россия",
            website = "www.deleted.com",
            createdAt = LocalDateTime.now(),
            deletedAt = deletedAt
        )

        assertThat(dto.deletedAt).isEqualTo(deletedAt)
        assertThat(dto.deletedAt).isNotNull()
    }

    @Test
    fun `CreateUniversityRequestDto should handle minimal fields`() {
        val dto = CreateUniversityRequestDto(
            name = "МИФИ",
            address = "",
            country = "Россия",
            website = ""
        )

        assertThat(dto.name).isEqualTo("МИФИ")
        assertThat(dto.address).isEmpty()
        assertThat(dto.country).isEqualTo("Россия")
        assertThat(dto.website).isEmpty()
    }

    @Test
    fun `UpdateUniversityRequestDto should update all fields with non-null values`() {
        val dto = UpdateUniversityRequestDto(
            name = "Новое имя",
            address = "Новый адрес",
            country = "Новая страна",
            website = "www.new-website.com"
        )

        assertThat(dto.name).isEqualTo("Новое имя")
        assertThat(dto.address).isEqualTo("Новый адрес")
        assertThat(dto.country).isEqualTo("Новая страна")
        assertThat(dto.website).isEqualTo("www.new-website.com")
    }

    @Test
    fun `UpdateUniversityRequestDto should work with empty strings`() {
        val dto = UpdateUniversityRequestDto(
            name = "",
            address = "",
            country = "",
            website = ""
        )

        assertThat(dto.name).isEmpty()
        assertThat(dto.address).isEmpty()
        assertThat(dto.country).isEmpty()
        assertThat(dto.website).isEmpty()
    }

    @Test
    fun `UniversityResponseDto should handle different countries correctly`() {
        val russianUniversity = UniversityResponseDto(
            id = UUID.randomUUID(),
            name = "КФУ",
            address = "ул. Кремлевская, 18, Казань",
            country = "Россия",
            website = "www.kpfu.ru",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        val usUniversity = UniversityResponseDto(
            id = UUID.randomUUID(),
            name = "Harvard University",
            address = "Cambridge, MA 02138",
            country = "USA",
            website = "www.harvard.edu",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        assertThat(russianUniversity.country).isEqualTo("Россия")
        assertThat(usUniversity.country).isEqualTo("USA")
        assertThat(russianUniversity).isNotEqualTo(usUniversity)
    }

    @Test
    fun `CreateUniversityRequestDto should handle website without protocol`() {
        val dto = CreateUniversityRequestDto(
            name = "ТГУ",
            address = "пр. Ленина, 36, Томск",
            country = "Россия",
            website = "www.tsu.ru"
        )

        assertThat(dto.website).isEqualTo("www.tsu.ru")
        assertThat(dto.website).doesNotContain("http://")
        assertThat(dto.website).doesNotContain("https://")
    }

    @Test
    fun `UniversityResponseDto should handle long names and addresses`() {
        val dto = UniversityResponseDto(
            id = UUID.randomUUID(),
            name = "Московский Государственный Университет имени М.В. Ломоносова",
            address = "Ленинские горы, д. 1, стр. 51, Москва, Россия, 119991",
            country = "Россия",
            website = "www.msu.ru",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        assertThat(dto.name).contains("Ломоносова")
        assertThat(dto.address).contains("119991")
    }
}