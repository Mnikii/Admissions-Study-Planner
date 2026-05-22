package com.eduplan.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
data class ApplicationProperties(
    val api: ApiProperties = ApiProperties(),
) {
    data class ApiProperties(
        val version: String = "v1",
        val basePath: String = "/api",
    )
}
