package io.raemian.api.support

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val tokenProvider: TokenProvider,
) : OncePerRequestFilter() {

    private val AUTHORIZATION_HEADER = "Authorization"
    private val BEARER_PREFIX = "Bearer "
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val jwt: String = resolveToken(request)

        if (tokenProvider.validateToken(jwt)) {
            val authentication: Authentication = tokenProvider.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            bearerToken
                .split(" ".toRegex())
                .last()
        } else {
            ""
        }
    }
}
