package com.eduplan.infrastructure.storage

import com.eduplan.application.port.output.FileStoragePort
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap

@Component
class MockFileStorageImpl : FileStoragePort {
    private val store = ConcurrentHashMap<String, ByteArray>()

    override fun store(path: String, content: InputStream, contentLength: Long, contentType: String): String {
        val out = ByteArrayOutputStream()
        content.copyTo(out)
        val bytes = out.toByteArray()
        store[path] = bytes
        return path
    }

    override fun delete(path: String) {
        store.remove(path)
    }

    override fun getResource(path: String): Resource? {
        return store[path]?.let { ByteArrayResource(it) }
    }
}
