package com.qmit.commerce.tracing

import io.micrometer.tracing.Tracer
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TraceIdFilter(
    private val tracer: Tracer
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val traceId = tracer.currentSpan()?.context()?.traceId() ?: request.getHeaders("X-Request-Id")
        if(traceId != null){
            MDC.put("traceId", traceId.toString())
            response.setHeader("X-Trace-Id", traceId.toString())
        }
        try{ filterChain.doFilter(request, response) } finally {
            MDC.remove("traceId")
        }
    }
}