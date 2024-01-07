package io.raemian.api.support

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SecurityException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtExceptionFilter : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(javaClass)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: SecurityException) {
            sendError(JwtErrorResponse(JwtErrorCode.UNEXPECTED, e.message), response)
        } catch (e: MalformedJwtException) {
            sendError(JwtErrorResponse(JwtErrorCode.MALFORMED, e.message), response)
        } catch (e: ExpiredJwtException) {
            sendError(JwtErrorResponse(JwtErrorCode.EXPIRED, e.message, e.claims.expiration), response)
        } catch (e: UnsupportedJwtException) {
            sendError(JwtErrorResponse(JwtErrorCode.UNSUPPORTED, e.message), response)
        } catch (e: IllegalArgumentException) {
            sendError(JwtErrorResponse(JwtErrorCode.ILLEGAL, e.message), response)
        }
    }

    // TODO: edit error response -> common error response
    private fun sendError(error: JwtErrorResponse, response: HttpServletResponse) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.write(toJson(error))
    }

    private fun toJson(data: Any): String {
        return ObjectMapper().writeValueAsString(data)
    }
}

data class JwtErrorResponse(
    val code: JwtErrorCode,
    val message: String?,
    val expiration: Date? = null,
)

enum class JwtErrorCode {
    UNEXPECTED,
    MALFORMED,
    EXPIRED,
    UNSUPPORTED,
    ILLEGAL,
}
