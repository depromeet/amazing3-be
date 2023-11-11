package io.raemian.springboot.core.auth.config

import io.raemian.springboot.core.auth.support.TokenProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.filter.CorsFilter


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val corsFilter: CorsFilter,
    private val tokenProvider: TokenProvider
) {

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
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,)
                    }
                    .accessDeniedHandler { request, response, accessDeniedException ->
                        // 필요한 권한이 없이 접근하려 할때 403
                        response.sendError(HttpServletResponse.SC_FORBIDDEN)
                    }
            }
            .authorizeHttpRequests {
                it.requestMatchers(AntPathRequestMatcher("/auth/**")).permitAll()
                    .anyRequest().authenticated()
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
        }
    }

    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // @Bean
    fun authorizedClientManager(
        clientRegistrationRepository: ClientRegistrationRepository,
        authorizedClientRepository: OAuth2AuthorizedClientRepository,
    ): OAuth2AuthorizedClientManager {
        val authorizedClientProvider: OAuth2AuthorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .refreshToken()
            .clientCredentials()
            .password()
            .build()
        val authorizedClientManager = DefaultOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientRepository,
        )
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)
        return authorizedClientManager
    }

}
