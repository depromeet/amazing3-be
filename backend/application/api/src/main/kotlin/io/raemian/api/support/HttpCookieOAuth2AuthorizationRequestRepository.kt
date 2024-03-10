package io.raemian.api.support

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class HttpCookieOAuth2AuthorizationRequestRepository() : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private val AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
    private val EXPIRE_SECONDS: Int = Duration.ofSeconds(180).toMillis().toInt()

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        val state = this.getStateParameter(request) ?: return null

        val authorizationRequest: OAuth2AuthorizationRequest? =
            CookieUtils.getCookie(request, AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map { cookie: Cookie ->
                    CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest::class.java)
                }.orElse(null)

        return if (authorizationRequest != null && state == authorizationRequest.state) {
            authorizationRequest
        } else {
            null
        }
    }
    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response)
            return
        }

        CookieUtils.addCookie(
            response,
            AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieUtils.serialize(authorizationRequest),
            EXPIRE_SECONDS,
        )
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): OAuth2AuthorizationRequest? {
        val authorizationRequest: OAuth2AuthorizationRequest? = this.loadAuthorizationRequest(request)

        if (authorizationRequest != null) {
            removeAuthorizationRequestCookies(request, response)
        }

        return authorizationRequest
    }

    private fun removeAuthorizationRequestCookies(request: HttpServletRequest, response: HttpServletResponse) {
        CookieUtils.deleteCookie(request, response, AUTHORIZATION_REQUEST_COOKIE_NAME)
    }

    private fun getStateParameter(request: HttpServletRequest): String? = request.getParameter("state")
}
