package io.raemian.api.support.security

import io.raemian.api.auth.model.Token
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class LoginRedirector(
    @Value("\${spring.profiles.active:local}")
    private val profile: String,
    val loginRequestRefererStorage: LoginRequestRefererStorage,
) {
    fun getUrl(state: String, token: Token): String {
        if (profile == "live") {
            return "https://bandiboodi.com/oauth2/token?token=${token.accessToken}&refresh=${token.refreshToken}"
        }

        val referer = loginRequestRefererStorage.get(state)

        if (profile == "dev" && referer.contains("dev-bandiboodi")) {
            return "https://dev-bandiboodi.vercel.app/oauth2/token?token=${token.accessToken}&refresh=${token.refreshToken}"
        }

        return "http://localhost:3000/oauth2/token?token=${token.accessToken}&refresh=${token.refreshToken}"
    }
}
