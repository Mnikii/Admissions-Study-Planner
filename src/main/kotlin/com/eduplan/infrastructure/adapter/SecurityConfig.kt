package com.eduplan.infrastructure.adapter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.http.HttpStatus
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.invoke
import com.eduplan.infrastructure.security.JwtAuthenticationFilter
import com.eduplan.presentation.dto.ErrorResponseDto
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.access.AccessDeniedHandler
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletRequest
import java.nio.charset.StandardCharsets
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    @Value("\${application.security.password.strength:8}")
    private val passwordStrength: Int,
) {
    private val objectMapper = ObjectMapper()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            anonymous { disable() }
            exceptionHandling {
                authenticationEntryPoint = org.springframework.security.web.AuthenticationEntryPoint { _, response, _ ->
                    response.status = HttpStatus.UNAUTHORIZED.value()
                    response.contentType = "application/json"
                    response.characterEncoding = StandardCharsets.UTF_8.name()
                    objectMapper.writeValue(response.writer, ErrorResponseDto("Unauthorized"))
                }
                accessDeniedHandler = AccessDeniedHandler { _: HttpServletRequest, response: HttpServletResponse, _ ->
                    response.status = HttpStatus.FORBIDDEN.value()
                    response.contentType = "application/json"
                    response.characterEncoding = StandardCharsets.UTF_8.name()
                    objectMapper.writeValue(response.writer, ErrorResponseDto("Forbidden"))
                }
            }
            authorizeHttpRequests {
                authorize("/api/v1/auth/register", permitAll)
                authorize("/api/v1/auth/login", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/actuator/health/**", permitAll)
                authorize("/api/v1/**", hasAnyRole("ADMIN", "USER"))
                authorize(anyRequest, authenticated)
            }
            formLogin { disable() }
            csrf {
                disable()
            }
        }
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:8080", "http://localhost:3000", "*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.exposedHeaders = listOf("Authorization", "X-Request-Id", "X-Correlation-Id")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(passwordStrength)

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager
}