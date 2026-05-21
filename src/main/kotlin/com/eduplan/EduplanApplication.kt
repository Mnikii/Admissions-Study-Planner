package com.eduplan

import com.eduplan.domain.model.Properties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(Properties::class)
class EduplanApplication

fun main(args: Array<String>) {
	runApplication<EduplanApplication>(*args)
}
