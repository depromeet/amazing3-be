package io.raemian.api.support.security

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class StateOAuth2AuthorizationRequestRepository(
    val profilePlaceHolder: ProfileHolder,
    val loginRequestRefererStorage: LoginRequestRefererStorage,
) : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private val log = LoggerFactory.getLogger(javaClass)

    private val oauthRequestStorage: Cache<String, OAuth2AuthorizationRequest> = Caffeine.newBuilder()
        .expireAfterWrite(60L, TimeUnit.SECONDS)
        .build()

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return oauthRequestStorage.getIfPresent(getState(request))
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        if (authorizationRequest != null) {
            /*** for frontend dev test ***/
            if (profilePlaceHolder.isDev()) {
                loginRequestRefererStorage.put(authorizationRequest.state, getReferer(request))
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

    private fun getState(request: HttpServletRequest): String = request.getParameter("state")

    private fun getReferer(request: HttpServletRequest): String = request.getHeader("referer")
}
