package com.eduplan.infrastructure.adapter

import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MdcTestController {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/mdc-test")
    fun testMdc(): Map<String, String> {
        log.info("=== MDC TEST ===")
        log.info("correlationId: ${MDC.get("correlationId")}")
        log.info("userId: ${MDC.get("userId")}")
        log.info("requestId: ${MDC.get("requestId")}")
        log.info("clientIp: ${MDC.get("clientIp")}")

        return mapOf(
            "status" to "ok",
            "message" to "Check console logs for MDC values",
            "correlationId" to (MDC.get("correlationId") ?: "NOT SET"),
            "userId" to (MDC.get("userId") ?: "NOT SET")
        )
    }
}