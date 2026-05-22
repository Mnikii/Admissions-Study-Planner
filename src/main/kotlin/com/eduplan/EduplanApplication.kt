package com.eduplan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EduplanApplication

fun main(args: Array<String>) {
	runApplication<EduplanApplication>(*args)
}
