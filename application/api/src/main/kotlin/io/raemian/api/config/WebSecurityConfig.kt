package io.raemian.api.config

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.auth.service.OAuth2UserService
import io.raemian.api.support.TokenProvider
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.filter.CorsFilter
import java.nio.charset.StandardCharsets

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val corsFilter: CorsFilter,
    private val tokenProvider: TokenProvider,
    private val oAuth2UserService: OAuth2UserService,
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it
                    .authenticationEntryPoint { request, response, authException ->
                        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                    }
                    .accessDeniedHandler { request, response, accessDeniedException ->
                        // 필요한 권한이 없이 접근하려 할때 403
                        response.sendError(HttpServletResponse.SC_FORBIDDEN)
                    }
            }
            .authorizeHttpRequests {
                it.requestMatchers(AntPathRequestMatcher("/auth/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/oauth2/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/login/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/one-baily-actuator/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/log/**")).permitAll()
                    .requestMatchers(
                        AntPathRequestMatcher("/swagger*/**"),
                        AntPathRequestMatcher("/v3/api-docs/**"),
                        AntPathRequestMatcher("/swagger-resources/**"),
                        AntPathRequestMatcher("/webjars/**"),
                    ).permitAll()
                    .anyRequest().permitAll()
            }
            .oauth2Login {
                it.userInfoEndpoint { endpoint -> endpoint.userService(oAuth2UserService) }
                it.successHandler { request, response, authentication ->
                    val user = authentication.principal as CurrentUser
                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.characterEncoding = StandardCharsets.UTF_8.name()

                    val tokenDTO = tokenProvider.generateTokenDto(user)
                    response.setHeader("x-token", tokenDTO.accessToken)
                    // TODO edit redirect url
                    response.sendRedirect("https://www.bandiboodi.com/login/oauth2/code/google?token=${tokenDTO.accessToken}&refresh=${tokenDTO.refreshToken}")
                }
                it.failureHandler { request, response, exception ->
                    response.addHeader("x-token", exception.message)
                }
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .apply(JwtSecurityConfig(tokenProvider))

        return http.build()
    }

    @Bean
    @ConditionalOnProperty(name = ["spring.h2.console.enabled"], havingValue = "true")
    fun configureH2ConsoleEnable(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it
                .ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(AntPathRequestMatcher("/favicon.ico", "**/favicon.ico"))
        }
    }

    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
