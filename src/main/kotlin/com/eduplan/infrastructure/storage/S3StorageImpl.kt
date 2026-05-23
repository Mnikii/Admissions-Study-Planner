package com.eduplan.infrastructure.storage

import com.eduplan.application.port.output.FileStoragePort
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.InputStream

/**
 * S3StorageImpl is temporarily disabled due to AWS SDK version incompatibility.
 * Use LocalFileStorageImpl or MockFileStorageImpl instead.
 * This will be re-enabled in a future release with corrected SDK configuration.
 */
@Component
@ConditionalOnProperty(prefix = "app.storage", name = ["type"], havingValue = "s3")
class S3StorageImpl : FileStoragePort {
    private val log = LoggerFactory.getLogger(S3StorageImpl::class.java)

    override fun store(path: String, content: InputStream, contentLength: Long, contentType: String): String {
        log.warn("S3StorageImpl.store() called but S3 is not properly configured. Using fallback.")
        return "s3://disabled/$path"
    }

    override fun delete(path: String) {
        log.warn("S3StorageImpl.delete() called but S3 is not properly configured.")
    }

    override fun getResource(path: String): Resource? {
        log.warn("S3StorageImpl.getResource() called but S3 is not properly configured.")
        return null
    }
}
