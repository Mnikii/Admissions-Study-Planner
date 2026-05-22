package com.eduplan.infrastructure.adapter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.config.annotation.web.invoke

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/api/v1/auth/register", permitAll)
                authorize(HttpMethod.GET, "/api/v1/**", hasAnyRole("ADMIN", "USER"))
                authorize(HttpMethod.POST, "/api/v1/**", hasRole("ADMIN"))
                authorize(HttpMethod.PUT, "/api/v1/**", hasRole("ADMIN"))
                authorize(HttpMethod.DELETE, "/api/v1/**", hasRole("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            httpBasic { }
            csrf {
                disable()
            }
        }
        return http.build()
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}