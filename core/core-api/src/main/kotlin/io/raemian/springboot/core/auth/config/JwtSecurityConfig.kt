package io.raemian.springboot.core.auth.config

import io.raemian.springboot.core.auth.support.JwtFilter
import io.raemian.springboot.core.auth.support.TokenProvider
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler


class JwtSecurityConfig(
    private val tokenProvider: TokenProvider,
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain?, HttpSecurity>() {

    // TokenProvider 를 주입받아서 JwtFilter 를 통해 Security 로직에 필터를 등록
    override fun configure(http: HttpSecurity) {
        val customFilter = JwtFilter(tokenProvider)
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}

