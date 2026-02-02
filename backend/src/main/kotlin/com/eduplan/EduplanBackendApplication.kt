package com.eduplan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EduplanBackendApplication

fun main(args: Array<String>) {
    runApplication<EduplanBackendApplication>(*args)
}
