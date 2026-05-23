package com.eduplan.infrastructure.storage

import com.eduplan.application.port.output.FileStoragePort
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Component
@ConditionalOnProperty(prefix = "app.storage", name = ["type"], havingValue = "local", matchIfMissing = true)
class LocalFileStorageImpl : FileStoragePort {
    private val base = Path.of("uploads")

    override fun store(path: String, content: InputStream, contentLength: Long, contentType: String): String {
        val resolved = base.resolve(path.removePrefix("uploads/"))
        Files.createDirectories(resolved.parent)
        Files.copy(content, resolved, StandardCopyOption.REPLACE_EXISTING)
        return resolved.toAbsolutePath().toString()
    }

    override fun delete(path: String) {
        try {
            Files.deleteIfExists(Path.of(path))
        } catch (e: Exception) {
            // log but ignore
        }
    }

    override fun getResource(path: String): Resource? {
        val p = Path.of(path)
        return if (Files.exists(p)) FileSystemResource(p) else null
    }
}
