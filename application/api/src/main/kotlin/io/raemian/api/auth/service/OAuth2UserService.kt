package io.raemian.api.auth.service

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.auth.domain.OAuthProvider
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserService(
    private val userRepository: UserRepository,
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val usernameAttributeName = userRequest.clientRegistration
            .providerDetails
            .userInfoEndpoint
            .userNameAttributeName

        return when (val provider = OAuthProvider.valueOf(userRequest.clientRegistration.registrationId.uppercase())) {
            OAuthProvider.GOOGLE -> {
                val email = oAuth2User.attributes["email"]?.toString() ?: throw RuntimeException("이메일이없음")
                val name = oAuth2User.attributes["name"]
                val user = upsert(email, OAuthProvider.GOOGLE)
                CurrentUser(
                    id = user.id!!,
                    email = email,
                    provider = provider.name,
                    password = "",
                    authorities = listOf(),
                )
            }

            OAuthProvider.NAVER -> {
                val userInfo = oAuth2User.attributes[usernameAttributeName] as Map<String, String>
                val email = userInfo["email"] ?: throw RuntimeException("이메일없음")
                val user = upsert(email, OAuthProvider.NAVER)
                CurrentUser(
                    id = user.id!!,
                    email = email,
                    provider = provider.name,
                    password = "",
                    authorities = listOf(),
                )
            }
        }
    }

    private fun upsert(email: String, oAuthProvider: OAuthProvider): User {
        return userRepository.findByEmail(email)
            ?: return userRepository.save(User(email = email, password = "", authority = Authority.ROLE_USER))
    }
}
