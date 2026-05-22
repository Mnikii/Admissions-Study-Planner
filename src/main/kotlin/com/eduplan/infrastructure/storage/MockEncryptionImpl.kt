package com.eduplan.infrastructure.storage

import com.eduplan.application.port.out.EncryptionPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MockEncryptionImpl : EncryptionPort {
    private val log = LoggerFactory.getLogger(MockEncryptionImpl::class.java)

    override fun encrypt(data: ByteArray): ByteArray {
        log.info("Mock encrypt called: {} bytes", data.size)
        return data
    }

    override fun decrypt(data: ByteArray): ByteArray {
        log.info("Mock decrypt called: {} bytes", data.size)
        return data
    }
}
