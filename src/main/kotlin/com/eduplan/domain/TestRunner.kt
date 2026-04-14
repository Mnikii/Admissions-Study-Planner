package com.eduplan.domain

import com.eduplan.domain.services.AccountService
import com.eduplan.domain.services.LoggingService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class TestRunner (
    private val accountService: AccountService,
    private val logger: LoggingService = LoggingService()

): CommandLineRunner {

    fun registerStudent() {
        accountService.studentRegister(
            UUID.fromString("05c4ee29-f7de-4ef9-bb08-90d03b4191e6"),
            "test@example.com",
            "Vasya Vasilyev",
            "+7 987 654 32 10",
            LocalDateTime.now()
        )
    }

    fun registerUni() {
        accountService.uniRegister(
            UUID.fromString("bebd051c-4f50-4347-a80e-c0992bb5935b"),
            "Real University",
            "ul. Ulichnaya, d.17, str. 19",
            mutableMapOf(
                "IT" to mutableListOf(
                    "Program Engineering",
                    "Applied mathematics and computer science"
                )
            )
        )
    }
    fun unregisterStudent() {
        accountService.studentUnregister(UUID.fromString("05c4ee29-f7de-4ef9-bb08-90d03b4191e6"))
    }

    fun unregisterUni() {
        accountService.uniUnregister(UUID.fromString("bebd051c-4f50-4347-a80e-c0992bb5935b"))
    }

    override fun run(vararg args: String) {
        logger.debug("Started a student registration process.")
        registerStudent()

        logger.debug("Started an university registration process.")
        registerUni()
        logger.debug("Started a student unregistration process.")
        unregisterStudent()
        logger.debug("Started an university unregistration process.")
        unregisterUni()
    }

}

