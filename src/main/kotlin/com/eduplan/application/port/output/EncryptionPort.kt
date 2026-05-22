package com.eduplan.application.port.output

interface EncryptionPort {
    fun encrypt(data: ByteArray): ByteArray
    fun decrypt(data: ByteArray): ByteArray
}
