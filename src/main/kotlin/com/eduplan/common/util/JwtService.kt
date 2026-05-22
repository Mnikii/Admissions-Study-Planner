package com.eduplan.common.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService(
    @Value("\${application.security.jwt.secret-key}")
    private val secretKey: String,
    @Value("\${application.security.jwt.expiration}")
    private val jwtExpiration: Long,
    @Value("\${application.security.jwt.refresh-token.expiration}")
    private val refreshExpiration: Long,
) {
    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun extractUsername(token: String): String = extractClaim(token, Claims::getSubject)

    fun <T> extractClaim(
        token: String,
        claimsResolver: (Claims) -> T,
    ): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims =
        Jwts
            .parser()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body

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
    ): String =
        Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()

    fun isTokenValid(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date())

    private fun extractExpiration(token: String): Date = extractClaim(token, Claims::getExpiration)

    fun extractExpirationDate(token: String): Date = extractExpiration(token)
}
