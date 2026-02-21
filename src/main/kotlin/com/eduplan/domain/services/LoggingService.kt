package com.eduplan.domain.services

import java.time.LocalDateTime

class LoggingService {

    fun info(message: String, tag: String = "COMMON") {
        println("[${LocalDateTime.now()}] [INFO] [$tag] $message")
    }
    fun warning(message: String, tag: String = "COMMON") {
        println("[${LocalDateTime.now()}] [WARNING] [$tag] $message")
    }
    fun error(message: String, tag: String = "COMMON") {
        println("[${LocalDateTime.now()}] [ERROR] [$tag] $message")
    }
    fun debug(message: String, tag: String = "COMMON") {
        println("[${LocalDateTime.now()}] [DEBUG] [$tag] $message")
    }

}