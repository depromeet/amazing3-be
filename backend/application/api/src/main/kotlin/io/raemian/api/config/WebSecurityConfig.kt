package io.raemian.api.config

import io.raemian.api.auth.converter.TokenRequestEntityConverter
import io.raemian.api.auth.model.CurrentUser
import io.raemian.api.auth.service.OAuth2UserService
import io.raemian.api.support.constant.WebSecurityConstant
import io.raemian.api.support.security.LoginRedirector
import io.raemian.api.support.security.StateOAuth2AuthorizationRequestRepository
import io.raemian.api.support.security.TokenProvider
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.CorsFilter
import java.nio.charset.StandardCharsets

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val corsFilter: CorsFilter,
    private val tokenProvider: TokenProvider,
    private val oAuth2UserService: OAuth2UserService,
    private val loginRedirector: LoginRedirector,
    private val tokenRequestEntityConverter: TokenRequestEntityConverter,
    private val httpCookieOAuth2AuthorizationRequestRepository: StateOAuth2AuthorizationRequestRepository,
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
                it.authenticationEntryPoint { request, response, authException ->
                    // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                }.accessDeniedHandler { request, response, accessDeniedException ->
                    // 필요한 권한이 없이 접근하려 할때 403
                    response.sendError(HttpServletResponse.SC_FORBIDDEN)
                }
            }
            .authorizeHttpRequests {
                it.requestMatchers(*WebSecurityConstant.PUBLIC_URIS).permitAll().anyRequest().authenticated()
            }
            .oauth2Login {
                it.tokenEndpoint { it.accessTokenResponseClient(accessTokenResponseClient()) }
                it.userInfoEndpoint { it.userService(oAuth2UserService) }
                it.authorizationEndpoint { it.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository) }
                it.successHandler { request, response, authentication ->
                    val user = authentication.principal as CurrentUser
                    val token = tokenProvider.generateTokenDto(user)
                    val redirectUrl = loginRedirector.getUrl(request.getParameter("state"), token)
                    response.sendRedirect(redirectUrl)
                }
                it.failureHandler { request, response, exception ->
                    log.error("x-token error ${exception.message}")
                    response.addHeader("x-token", exception.message)
                }
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .apply(JwtSecurityConfig(tokenProvider))

        return http.build()
    }

    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun accessTokenResponseClient(): OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
        val accessTokenResponseClient = DefaultAuthorizationCodeTokenResponseClient()
        accessTokenResponseClient.setRequestEntityConverter(tokenRequestEntityConverter)

        return accessTokenResponseClient
    }
}
