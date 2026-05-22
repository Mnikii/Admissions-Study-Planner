package com.eduplan.application.port.output

import org.springframework.core.io.Resource
import java.io.InputStream

interface FileStoragePort {
    fun store(path: String, content: InputStream, contentLength: Long, contentType: String): String
    fun delete(path: String)
    fun getResource(path: String): Resource?
}
