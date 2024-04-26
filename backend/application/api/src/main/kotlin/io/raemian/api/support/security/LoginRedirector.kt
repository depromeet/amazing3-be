package io.raemian.api.support.security

import io.raemian.api.auth.model.Token
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LoginRedirector(
    val profilePlaceHolder: ProfileHolder,
    val redirectUrlHolder: RedirectUrlHolder,
    val loginRequestRefererStorage: LoginRequestRefererStorage,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getUrl(state: String, token: Token): String {
        if (profilePlaceHolder.isLive()) {
            return "${redirectUrlHolder.live}?token=${token.accessToken}&refresh=${token.refreshToken}"
        }

        val referer = loginRequestRefererStorage.get(state)

        if (profilePlaceHolder.isDev() && referer.contains("dev-bandiboodi")) {
            return "${redirectUrlHolder.dev}?token=${token.accessToken}&refresh=${token.refreshToken}"
        }

        return "${redirectUrlHolder.local}?token=${token.accessToken}&refresh=${token.refreshToken}"
    }
}
