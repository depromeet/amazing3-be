package io.raemian.api.support.security

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class StateOAuth2AuthorizationRequestRepository(
    @Value("\${spring.profiles.active}")
    private val profile: String,
    val loginRequestRefererStorage: LoginRequestRefererStorage,
) : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private val oauthRequestStorage: Cache<String, OAuth2AuthorizationRequest> = Caffeine.newBuilder()
        .expireAfterWrite(60L, TimeUnit.SECONDS)
        .build()

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return oauthRequestStorage.getIfPresent(getStateParameter(request))
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        if (authorizationRequest != null) {
            /*** for frontend dev test ***/
            if (profile == "dev") {
                loginRequestRefererStorage.put(authorizationRequest.state, request.getParameter("referer"))
            }

            oauthRequestStorage.put(authorizationRequest.state, authorizationRequest)
        }
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): OAuth2AuthorizationRequest? {
        return loadAuthorizationRequest(request)
    }

    private fun getStateParameter(request: HttpServletRequest): String = request.getParameter("state")
}
