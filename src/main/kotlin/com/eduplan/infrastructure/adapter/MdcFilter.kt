package com.eduplan.infrastructure.adapter

import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.util.UUID
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class MdcFilter : Filter {

    companion object {
        private const val CORRELATION_ID_HEADER = "X-Correlation-Id"
        private const val REQUEST_ID_HEADER = "X-Request-Id"
        private const val USER_ID_HEADER = "X-User-Id"

        const val MDC_CORRELATION_ID = "correlationId"
        const val MDC_REQUEST_ID = "requestId"
        const val MDC_USER_ID = "userId"
        const val MDC_SESSION_ID = "sessionId"
        const val MDC_CLIENT_IP = "clientIp"
        const val MDC_REQUEST_URI = "requestUri"
        const val MDC_REQUEST_METHOD = "requestMethod"
    }

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        try {
            val correlationId = httpRequest.getHeader(CORRELATION_ID_HEADER)
                ?: UUID.randomUUID().toString()

            val requestId = httpRequest.getHeader(REQUEST_ID_HEADER)
                ?: UUID.randomUUID().toString()

            val userId = extractUserId(httpRequest)
            MDC.put(MDC_CORRELATION_ID, correlationId)
            MDC.put(MDC_REQUEST_ID, requestId)
            MDC.put(MDC_USER_ID, userId)
            MDC.put(MDC_SESSION_ID, httpRequest.session?.id)
            MDC.put(MDC_CLIENT_IP, getClientIp(httpRequest))
            MDC.put(MDC_REQUEST_URI, httpRequest.requestURI)
            MDC.put(MDC_REQUEST_METHOD, httpRequest.method)

            httpResponse.setHeader(CORRELATION_ID_HEADER, correlationId)
            httpResponse.setHeader(REQUEST_ID_HEADER, requestId)

            chain.doFilter(request, response)

        } finally {

            MDC.clear()
        }
    }

    private fun extractUserId(request: HttpServletRequest): String {

        val headerUserId = request.getHeader(USER_ID_HEADER)
        if (!headerUserId.isNullOrBlank()) return headerUserId


        return "anonymous"
    }

    private fun getClientIp(request: HttpServletRequest): String {
        var ip = request.getHeader("X-Forwarded-For")
        if (ip.isNullOrBlank()) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip.isNullOrBlank()) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip.isNullOrBlank()) {
            ip = request.getHeader("HTTP_CLIENT_IP")
        }
        if (ip.isNullOrBlank()) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR")
        }
        if (ip.isNullOrBlank()) {
            ip = request.remoteAddr
        }
        return ip ?: "unknown"
    }
}