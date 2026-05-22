package com.eduplan.infrastructure.encryption

import com.eduplan.application.port.output.EncryptionPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MockEncryptionImpl : EncryptionPort {
    private val log = LoggerFactory.getLogger(MockEncryptionImpl::class.java)

    override fun encrypt(data: ByteArray): ByteArray {
        log.info("Mock encrypt called, returning original bytes")
        return data
    }

    override fun decrypt(data: ByteArray): ByteArray {
        log.info("Mock decrypt called, returning original bytes")
        return data
    }
}
