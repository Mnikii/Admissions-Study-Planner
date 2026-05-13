package com.eduplan

import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class RequestIdFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val requestId = UUID.randomUUID().toString()
        MDC.put("requestId", requestId)
        response.setHeader("X-Request-Id", requestId)

        chain.doFilter(request, response)

        MDC.clear()
    }
}