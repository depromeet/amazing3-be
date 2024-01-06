package io.raemian.api.repository

import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goalsandpublication.GoalsAndPublicationRepository
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.enums.OAuthProvider
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
class GoalsAndPublicationRepositoryTest {

    companion object {
        val USER_FIXTURE = User(
            email = "dfghcvb111@naver.com",
            userName = "binaryHoHo",
            nickname = "binaryHoHoHo",
            birth = LocalDate.MIN,
            image = "",
            provider = OAuthProvider.NAVER,
            authority = Authority.ROLE_USER,
            isGoalsPublic = true,
        )

        val STICKER_FIXTURE = Sticker("sticker", "image yeah")
        val TAG_FIXTURE = Tag("꿈")
    }

    @Autowired
    lateinit var goalsAndPublicationRepository: GoalsAndPublicationRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @BeforeEach
    fun saveEntities() {
        entityManager.merge(USER_FIXTURE)
        entityManager.merge(STICKER_FIXTURE)
        entityManager.merge(TAG_FIXTURE)
    }

    @Test
    @Transactional
    @DisplayName("UserName을 통해 다른 유저의 전체 Goals와 공개 여부를 확인할 수 있다.")
    fun findLifeMapByUserNameTest() {
        val USER_FIXTURE2 = User(
            email = "22@naver.com",
            userName = "22",
            nickname = "22",
            birth = LocalDate.MIN,
            image = "",
            provider = OAuthProvider.NAVER,
            authority = Authority.ROLE_USER,
            isGoalsPublic = false,
        )
        entityManager.merge(USER_FIXTURE2)

        val USER_FIXTURE3 = User(
            email = "33@naver.com",
            userName = "3333",
            nickname = "3333",
            birth = LocalDate.MIN,
            image = "",
            provider = OAuthProvider.NAVER,
            authority = Authority.ROLE_USER,
            isGoalsPublic = false,
        )
        entityManager.merge(USER_FIXTURE3)

        val myGoal = Goal(
            user = USER_FIXTURE,
            title = "제목1",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            tasks = emptyList(),
        )

        val anotherUsersGoal1 = Goal(
            user = USER_FIXTURE2,
            title = "제목2",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            tasks = emptyList(),
        )

        val anotherUsersGoal2 = Goal(
            user = USER_FIXTURE2,
            title = "제목3",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            tasks = emptyList(),
        )

        entityManager.merge(myGoal)
        entityManager.merge(anotherUsersGoal1)
        entityManager.merge(anotherUsersGoal2)

        val goalsAndPublication =
            goalsAndPublicationRepository.findGoalsAndPublicationByUserName(USER_FIXTURE2.userName!!)
        assertThat(goalsAndPublication.isGoalsPublic).isFalse()
        assertThat(goalsAndPublication.goals.size).isEqualTo(2)
        assertThat(goalsAndPublication.goals[0].id).isEqualTo(anotherUsersGoal1.id)
        assertThat(goalsAndPublication.goals[1].id).isEqualTo(anotherUsersGoal2.id)
    }

    @Test
    @Transactional
    @DisplayName("유저가 Goal을 갖고 있지 않아도 해당 User의 Goal 공개 여부를 확인할 수 있다.")
    fun findGoalsAndPublicationByUserNameTest2() {
        val USER_FIXTURE2 = User(
            email = "22@naver.com",
            userName = "22",
            nickname = "22",
            birth = LocalDate.MIN,
            image = "",
            provider = OAuthProvider.NAVER,
            authority = Authority.ROLE_USER,
            isGoalsPublic = false,
        )
        entityManager.merge(USER_FIXTURE2)


        val goalsAndPublication =
            goalsAndPublicationRepository.findGoalsAndPublicationByUserName(USER_FIXTURE2.userName!!)
        assertThat(goalsAndPublication.isGoalsPublic).isFalse()
    }
}
