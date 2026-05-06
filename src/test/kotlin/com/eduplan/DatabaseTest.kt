package com.eduplan

import com.eduplan.test.infrastucture.IntegrationTestBase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import java.sql.DriverManager

class DatabaseTest : IntegrationTestBase() {

    @Test
    fun testDatabaseCreation() {
        // H2 database - не требует Docker
        val connection = DriverManager.getConnection(
            "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
            "sa",
            ""
        )

        connection.createStatement().execute("CREATE TABLE test (id SERIAL PRIMARY KEY, name TEXT)")

        val result = connection.createStatement().executeQuery(
            "SELECT table_name FROM information_schema.tables WHERE table_name = 'TEST'"
        )

        assertTrue(result.next())
        println("База данных создана и таблица test существует!")

        connection.close()
    }
}