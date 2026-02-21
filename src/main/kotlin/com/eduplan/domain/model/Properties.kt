package com.eduplan.domain.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "application")
data class Properties(
    val name: String,
    val restrictedNames: List<String>
)