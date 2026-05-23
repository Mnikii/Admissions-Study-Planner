package com.eduplan.infrastructure.storage

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "app.storage", name = ["type"], havingValue = "s3")
@ConfigurationProperties(prefix = "app.s3")
class S3StorageConfig {
    lateinit var bucket: String
    lateinit var region: String
    lateinit var accessKey: String
    lateinit var secretKey: String
}
