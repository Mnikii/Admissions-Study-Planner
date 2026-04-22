package com.eduplan.test.domain.model

import com.eduplan.domain.model.University
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class UniversityTest {

    @Test
    fun `should create university with all required parameters`() {
        val universityId = UUID.randomUUID()
        val createdAt = LocalDateTime.of(2024, 1, 1, 10, 0)

        val university = University(
            id = universityId,
            name = "Московский Государственный Университет",
            address = "Ленинские горы, 1, Москва",
            country = "Россия",
            website = "www.msu.ru",
            createdAt = createdAt,
            deletedAt = null
        )

        assertThat(university.id).isEqualTo(universityId)
        assertThat(university.name).isEqualTo("Московский Государственный Университет")
        assertThat(university.address).isEqualTo("Ленинские горы, 1, Москва")
        assertThat(university.country).isEqualTo("Россия")
        assertThat(university.website).isEqualTo("www.msu.ru")
        assertThat(university.createdAt).isEqualTo(createdAt)
        assertThat(university.deletedAt).isNull()
    }

    @Test
    fun `should create university with default values`() {
        val universityId = UUID.randomUUID()

        val university = University(
            id = universityId,
            name = "СПбГУ",
            address = "Университетская наб., 7-9, Санкт-Петербург",
            country = "Россия",
            website = "www.spbu.ru",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )

        assertThat(university.id).isNotNull()
        assertThat(university.createdAt).isNotNull()
        assertThat(university.deletedAt).isNull()
    }

    @Test
    fun `should support data class operations`() {
        val universityId = UUID.randomUUID()
        val createdAt = LocalDateTime.of(2024, 1, 1, 10, 0)

        val university1 = University(
            id = universityId,
            name = "МГУ",
            address = "Ленинские горы, 1, Москва",
            country = "Россия",
            website = "www.msu.ru",
            createdAt = createdAt,
            deletedAt = null
        )

        val university2 = university1.copy(name = "МГУ им. Ломоносова")
        val university3 = university1.copy(website = "www.msu.ru")

        assertThat(university1.name).isEqualTo("МГУ")
        assertThat(university2.name).isEqualTo("МГУ им. Ломоносова")
        assertThat(university1).isNotEqualTo(university2)
        assertThat(university1).isEqualTo(university3)
    }

    @Test
    fun `should handle soft delete correctly`() {
        val universityId = UUID.randomUUID()
        val createdAt = LocalDateTime.of(2024, 1, 1, 10, 0)

        val university = University(
            id = universityId,
            name = "НГУ",
            address = "ул. Пирогова, 1, Новосибирск",
            country = "Россия",
            website = "www.nsu.ru",
            createdAt = createdAt,
            deletedAt = null
        )

        assertThat(university.deletedAt).isNull()

        val deletedUniversity = university.copy(deletedAt = LocalDateTime.now())
        assertThat(deletedUniversity.deletedAt).isNotNull()
    }

    @Test
    fun `should handle different countries`() {
        val universityId = UUID.randomUUID()
        val createdAt = LocalDateTime.of(2024, 1, 1, 10, 0)

        val russianUniversity = University(
            id = universityId,
            name = "КФУ",
            address = "ул. Кремлевская, 18, Казань",
            country = "Россия",
            website = "www.kpfu.ru",
            createdAt = createdAt,
            deletedAt = null
        )

        val usUniversity = University(
            id = UUID.randomUUID(),
            name = "Harvard University",
            address = "Cambridge, MA 02138",
            country = "USA",
            website = "www.harvard.edu",
            createdAt = createdAt,
            deletedAt = null
        )

        assertThat(russianUniversity.country).isEqualTo("Россия")
        assertThat(usUniversity.country).isEqualTo("USA")
        assertThat(russianUniversity).isNotEqualTo(usUniversity)
    }

    @Test
    fun `should handle website URL formats`() {
        val universityId = UUID.randomUUID()
        val createdAt = LocalDateTime.of(2024, 1, 1, 10, 0)

        val universityWithHttp = University(
            id = universityId,
            name = "ТГУ",
            address = "пр. Ленина, 36, Томск",
            country = "Россия",
            website = "http://www.tsu.ru",
            createdAt = createdAt,
            deletedAt = null
        )

        val universityWithHttps = universityWithHttp.copy(website = "https://www.tsu.ru")
        val universityWithoutProtocol = universityWithHttp.copy(website = "www.tsu.ru")

        assertThat(universityWithHttp.website).isEqualTo("http://www.tsu.ru")
        assertThat(universityWithHttps.website).isEqualTo("https://www.tsu.ru")
        assertThat(universityWithoutProtocol.website).isEqualTo("www.tsu.ru")
    }

    @Test
    fun `should preserve createdAt timestamp on copy`() {
        val originalCreatedAt = LocalDateTime.of(2024, 1, 1, 10, 0)
        val universityId = UUID.randomUUID()

        val university = University(
            id = universityId,
            name = "ДВФУ",
            address = "о. Русский, Владивосток",
            country = "Россия",
            website = "www.dvfu.ru",
            createdAt = originalCreatedAt,
            deletedAt = null
        )

        val updatedUniversity = university.copy(
            name = "Дальневосточный Федеральный Университет",
            website = "www.dvfu.ru"
        )

        assertThat(updatedUniversity.createdAt).isEqualTo(originalCreatedAt)
        assertThat(updatedUniversity.name).isEqualTo("Дальневосточный Федеральный Университет")
    }

    @Test
    fun `should create university with minimal parameters`() {
        val universityId = UUID.randomUUID()
        val createdAt = LocalDateTime.now()

        val university = University(
            id = universityId,
            name = "МИФИ",
            address = "",
            country = "Россия",
            website = "",
            createdAt = createdAt,
            deletedAt = null
        )

        assertThat(university.name).isEqualTo("МИФИ")
        assertThat(university.address).isEmpty()
        assertThat(university.website).isEmpty()
        assertThat(university.country).isEqualTo("Россия")
    }
}