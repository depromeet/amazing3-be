package io.raemian.api.auth.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import io.raemian.api.auth.model.CurrentUser
import io.raemian.api.auth.model.OauthUserResult
import io.raemian.api.log.service.LogService
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import io.raemian.storage.db.core.user.enums.OAuthProvider
import net.minidev.json.JSONObject
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuth2UserService(
    private val userRepository: UserRepository,
    private val lifeMapRepository: LifeMapRepository,
    private val logService: LogService,
) : DefaultOAuth2UserService() {

    companion object {
        private const val USERNAME_PREFIX = "BANDIBOODI-"
    }

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val provider =
            OAuthProvider.valueOf(userRequest.clientRegistration.registrationId.uppercase())

        val userInfo = parseUserInfo(
            provider,
            userRequest,
        )

        val user = upsert(
            email = userInfo.email,
            image = userInfo.image,
            oAuthProvider = provider,
        )

        return CurrentUser(
            id = user.id!!,
            email = user.email,
            authorities = listOf(),
        )
    }

    @Transactional
    fun upsert(email: String, image: String, oAuthProvider: OAuthProvider): User {
        val user = userRepository.findByEmailAndProvider(email, oAuthProvider)
            ?: createUser(email, image, oAuthProvider)

        lifeMapRepository.findFirstByUserId(user.id!!)
            ?: createUserDefaultLifeMap(user)

        logService.upsertLatestLogin(user.id)

        return user
    }

    private fun createUser(email: String, image: String, oAuthProvider: OAuthProvider): User {
        val user = User(
            email = email,
            image = image,
            provider = oAuthProvider,
            authority = Authority.ROLE_USER,
        )

        userRepository.save(user)
        val updateUsername = updateUsernameAndNickname(user)
        createUserDefaultLifeMap(user)
        return updateUsername
    }

    private fun updateUsernameAndNickname(user: User): User {
        val update = user
            .updateUsername("$USERNAME_PREFIX${user.id!!}")
            .updateNickname("$USERNAME_PREFIX${user.id!!}")
        return userRepository.save(update)
    }

    private fun createUserDefaultLifeMap(user: User) {
        val lifeMap = LifeMap(user, true, goals = ArrayList())
        lifeMapRepository.save(lifeMap)
    }

    private fun decodeIdToken(idToken: String): JSONObject {
        val signedJWT = SignedJWT.parse(idToken)
        val getPayload: JWTClaimsSet = signedJWT.jwtClaimsSet
        val objectMapper = ObjectMapper()
        val payload: JSONObject = objectMapper.readValue(
            getPayload.toString(),
            JSONObject::class.java,
        )

        return payload
    }

    private fun parseUserInfo(provide: OAuthProvider, request: OAuth2UserRequest): OauthUserResult {
        return when (provide) {
            OAuthProvider.APPLE -> {
                val idToken = request.additionalParameters["id_token"].toString()

                val userInfo = decodeIdToken(idToken)

                OauthUserResult(email = userInfo.getAsString("email"), image = "")
            }
            OAuthProvider.GOOGLE -> {
                val oAuth2User: OAuth2User = super.loadUser(request)

                val email =
                    oAuth2User.attributes["email"]?.toString() ?: throw RuntimeException("구글 이메일이없음")
                val image = oAuth2User.attributes["picture"]?.toString() ?: ""

                OauthUserResult(email = email, image = image)
            }
            OAuthProvider.KAKAO -> {
                val oAuth2User = super.loadUser(request)
                val properties = oAuth2User.attributes["properties"] as Map<String, String>
                val account = oAuth2User.attributes["kakao_account"] as Map<String, String>

                val email = account["email"] ?: throw RuntimeException("카카오 이메일없음")
                val image = properties["profile_image"] ?: ""

                OauthUserResult(email = email, image = image)
            }
            OAuthProvider.NAVER -> {
                val oAuth2User: OAuth2User = super.loadUser(request)

                val usernameAttributeName = request.clientRegistration
                    .providerDetails.userInfoEndpoint
                    .userNameAttributeName

                val userInfo = oAuth2User.attributes[usernameAttributeName] as Map<String, String>
                val email = userInfo["email"] ?: throw RuntimeException("네이버 이메일없음")
                val image = userInfo["profile_image"] ?: ""

                OauthUserResult(email = email, image = image)
            }
        }
    }
}
