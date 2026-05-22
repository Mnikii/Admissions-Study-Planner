package com.eduplan.infrastructure.storage

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app.s3")
class S3StorageConfig {
    lateinit var bucket: String
    lateinit var region: String
    lateinit var accessKey: String
    lateinit var secretKey: String
}
