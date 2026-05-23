package com.eduplan.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService(
    @Value("\${application.security.jwt.secret-key:Zm9yLWRldmVsb3BtZW50LW9ubHktc2VjcmV0LWtleS0zMmJ5dGVzLW1pbmltdW0=}")
    private val secretKey: String,
    @Value("\${application.security.jwt.expiration:86400000}")
    private val jwtExpiration: Long,
    @Value("\${application.security.jwt.refresh-token.expiration:604800000}")
    private val refreshExpiration: Long,
) {
    private val mapper = ObjectMapper()

    private fun keyBytes(): ByteArray =
        try {
            Base64.getDecoder().decode(secretKey)
        } catch (_: Exception) {
            secretKey.toByteArray(StandardCharsets.UTF_8)
        }

    private fun base64UrlEncode(bytes: ByteArray): String =
        Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)

    private fun base64UrlDecode(value: String): ByteArray =
        Base64.getUrlDecoder().decode(value)

    private fun sign(input: String): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(keyBytes(), "HmacSHA256"))
        return mac.doFinal(input.toByteArray(StandardCharsets.UTF_8))
    }

    private fun parsePayload(token: String): Map<String, Any> {
        val parts = token.split('.')
        require(parts.size == 3) { "Malformed token" }

        val signingInput = "${parts[0]}.${parts[1]}"
        val expected = sign(signingInput)
        val actual = base64UrlDecode(parts[2])
        require(MessageDigest.isEqual(expected, actual)) { "Invalid token signature" }

        val payloadJson = String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8)
        @Suppress("UNCHECKED_CAST")
        return mapper.readValue(payloadJson, Map::class.java) as Map<String, Any>
    }

    fun extractUsername(token: String): String = parsePayload(token)["sub"].toString()

    fun <T> extractClaim(
        token: String,
        claimsResolver: (Map<String, Any>) -> T,
    ): T {
        val claims = parsePayload(token)
        return claimsResolver(claims)
    }

    fun generateToken(userDetails: UserDetails): String = generateToken(mapOf(), userDetails)

    fun generateToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
    ): String = buildToken(extraClaims, userDetails, jwtExpiration)

    fun generateRefreshToken(userDetails: UserDetails): String = buildToken(mapOf(), userDetails, refreshExpiration)

    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expiration: Long,
    ): String {
        val now = System.currentTimeMillis()
        val payload =
            LinkedHashMap<String, Any>().apply {
                putAll(extraClaims)
                put("sub", userDetails.username)
                put("iat", now / 1000)
                put("exp", (now + expiration) / 1000)
            }

        val headerJson = mapper.writeValueAsString(mapOf("alg" to "HS256", "typ" to "JWT"))
        val payloadJson = mapper.writeValueAsString(payload)
        val headerPart = base64UrlEncode(headerJson.toByteArray(StandardCharsets.UTF_8))
        val payloadPart = base64UrlEncode(payloadJson.toByteArray(StandardCharsets.UTF_8))
        val signingInput = "$headerPart.$payloadPart"
        val signature = base64UrlEncode(sign(signingInput))
        return "$signingInput.$signature"
    }

    fun isTokenValid(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date())

    private fun extractExpiration(token: String): Date {
        val payload = parsePayload(token)
        val expSeconds = (payload["exp"] as Number).toLong()
        return Date(expSeconds * 1000)
    }

    fun extractExpirationDate(token: String): Date = extractExpiration(token)
}
