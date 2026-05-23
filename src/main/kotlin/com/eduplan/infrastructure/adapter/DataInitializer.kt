package com.eduplan.infrastructure.adapter

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class DataInitializer(
    private val jdbcTemplate: JdbcTemplate,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val count = jdbcTemplate.queryForObject("SELECT count(*) FROM universities", Long::class.java)
            ?: 0L
        if (count > 0) return

        val samples = listOf(
            Triple("National University of Example", "123 Example St", "Exampleland"),
            Triple("Example State University", "45 College Ave", "Exampleland"),
            Triple("Institute of Examples", "7 Sample Rd", "Sampleland"),
            Triple("Examples International", "88 Demo Blvd", "Examplia"),
            Triple("College of Testing", "1 Test Way", "Testonia"),
            Triple("Demo University", "12 Demo Dr", "Demoland"),
            Triple("Sample Polytechnic", "99 Sample Pkwy", "Sampleland"),
            Triple("University of Mockups", "5 Mock St", "Mockland"),
            Triple("Academy of Trials", "77 Trial Ave", "Trialia"),
            Triple("Central Example College", "100 Central Rd", "Exampleland"),
        ).shuffled().take(7)

        val sql = """
            INSERT INTO universities (id, name, address, country, website, created_at)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT (name) DO NOTHING
        """.trimIndent()

        val now = LocalDateTime.now()
        for ((name, address, country) in samples) {
            try {
                jdbcTemplate.update(
                    sql,
                    UUID.randomUUID(),
                    name,
                    address,
                    country,
                    "https://www.${name.replace(" ", "").lowercase()}.edu",
                    now,
                )
            } catch (_: Exception) {
                // ignore individual insert errors (e.g., concurrent insert), continue
            }
        }
    }

    private fun arrayOfAny(vararg items: Any?) = items
}
