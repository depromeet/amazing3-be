package io.raemian.api.integration.auth

import io.raemian.api.auth.service.OAuth2UserService
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import io.raemian.storage.db.core.user.enums.OAuthProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
class OAuth2UserServiceTest {

    companion object {
        val USER_FIXTURE = User(
            email = "dfghcvb111@naver.com",
            username = "binaryHoHo",
            nickname = "binaryHoHoHo",
            birth = LocalDate.MIN,
            image = "",
            provider = OAuthProvider.NAVER,
            authority = Authority.ROLE_USER,
        )

        val LIFE_MAP_FIXTURE = LifeMap(USER_FIXTURE, true)
        val STICKER_FIXTURE = Sticker("sticker", "image yeah")
        val TAG_FIXTURE = Tag("꿈")
    }

    @Autowired
    private lateinit var oAuth2UserService: OAuth2UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var lifeMapRepository: LifeMapRepository

    @Test
    @DisplayName("OAuth 로그인시 가입되어 있지 않은 유저는 새로 저장된다.")
    @Transactional
    fun upsertTest() {
        // given
        val userBeforeSave = userRepository.findByEmail(USER_FIXTURE.email)

        // when
        oAuth2UserService.upsert(USER_FIXTURE.email, USER_FIXTURE.image, USER_FIXTURE.provider)
        val userAfterSave = userRepository.findByEmail(USER_FIXTURE.email)

        // then
        Assertions.assertAll(
            Executable {
                assertThat(userBeforeSave).isNull()
                assertThat(userAfterSave).isNotNull()
                assertThat(userAfterSave?.email).isEqualTo(USER_FIXTURE.email)
            },
        )
    }

    @Test
    @DisplayName("OAuth 회원가입시 기본적으로 LifeMap 한개가 주어진다.")
    @Transactional
    fun createUserDefaultLifeMapTest() {
        val lifeMapsBeforeSignUp = lifeMapRepository.findAll()
        val user =
            oAuth2UserService.upsert(USER_FIXTURE.email, USER_FIXTURE.image, USER_FIXTURE.provider)
        val lifeMapsAfterSignUp = lifeMapRepository.findFirstByUserId(user.id!!)

        Assertions.assertAll(
            Executable {
                assertThat(lifeMapsBeforeSignUp).isEmpty()
                assertThat(lifeMapsAfterSignUp).isNotNull
            },
        )
    }

    @Test
    @DisplayName("OAuth 회원가입시 Username이 'BANDIBOODI-'와 id값을 합친 String으로 주어진다. ")
    @Transactional
    fun updateUsernameTest() {
        oAuth2UserService.upsert(USER_FIXTURE.email, USER_FIXTURE.image, USER_FIXTURE.provider)
        val user = userRepository.findByEmail(USER_FIXTURE.email)
        assertThat(user?.username).isEqualTo("BANDIBOODI-${user?.id!!}")
    }
}
