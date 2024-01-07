package io.raemian.api.auth.service

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.support.TokenProvider
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import io.raemian.storage.db.core.user.enums.OAuthProvider
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserService(
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider,
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val usernameAttributeName = userRequest.clientRegistration
            .providerDetails.userInfoEndpoint
            .userNameAttributeName

        return when (val provider = OAuthProvider.valueOf(userRequest.clientRegistration.registrationId.uppercase())) {
            OAuthProvider.GOOGLE -> {
                val email = oAuth2User.attributes["email"]?.toString() ?: throw RuntimeException("이메일이없음")
                val name = oAuth2User.attributes["name"]?.toString()
                val image = oAuth2User.attributes["picture"]?.toString() ?: ""

                val user = upsert(email, image, OAuthProvider.GOOGLE)
                val current = CurrentUser(
                    id = user.id!!,
                    email = email,
                    authorities = listOf(),
                )
                val token = tokenProvider.generateTokenDto(current)
                saveRefreshToken(current.id, token.refreshToken)
                val copied = current.copy(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken,
                )

                copied
            }

            OAuthProvider.NAVER -> {
                val userInfo = oAuth2User.attributes[usernameAttributeName] as Map<String, String>
                val email = userInfo["email"] ?: throw RuntimeException("이메일없음")
                val image = userInfo["profile_image"] ?: ""
                val user = upsert(
                    email = email,
                    image = image,
                    oAuthProvider = provider,
                )
                CurrentUser(
                    id = user.id!!,
                    email = email,
                    authorities = listOf(),
                )
            }

            OAuthProvider.KAKAO -> {
                val id = oAuth2User.attributes["id"]?.toString() ?: ""
                val properties = oAuth2User.attributes["properties"] as Map<String, String>
                val profileImage = properties["profile_image"] ?: ""
                val thumbnailImage = properties["thumbnail_image"]
                val nickname = properties["nickname"]
                val email = properties["email"] ?: throw RuntimeException("이메일없음")
                val user = upsert(
                    email = id,
                    image = profileImage,
                    oAuthProvider = provider,
                )
                CurrentUser(
                    id = user.id!!,
                    email = user.email,
                    authorities = listOf(),
                )
            }
        }
    }

    private fun saveRefreshToken(userId: Long, refreshToken: String) {
    }

    private fun upsert(email: String, image: String, oAuthProvider: OAuthProvider): User {
        return userRepository.findByEmail(email)
            ?: return userRepository.save(
                User(
                    email = email,
                    image = image,
                    provider = oAuthProvider,
                    authority = Authority.ROLE_USER,
                ),
            )
    }
}
