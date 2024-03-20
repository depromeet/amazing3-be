package io.raemian.api.integration.lifemap

import io.raemian.api.lifemap.controller.request.UpdatePublicRequest
import io.raemian.api.lifemap.service.LifeMapService
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.enums.OAuthProvider
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
class LifeMapServiceTest {

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
    private lateinit var lifeMapService: LifeMapService

    @Autowired
    private lateinit var lifeMapRepository: LifeMapRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun saveEntities() {
        entityManager.merge(USER_FIXTURE)
        entityManager.merge(LIFE_MAP_FIXTURE)
        entityManager.merge(STICKER_FIXTURE)
        entityManager.merge(TAG_FIXTURE)
    }

    @Test
    @DisplayName("User ID를 통해 유저가 가진 전체 Goal을 조회 할 수 있다.")
    @Transactional
    fun findAllByUserIdTest() {
        // given
        val goal1 = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목1",
            deadline = LocalDate.now(),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            lastCommentReadAt = LocalDateTime.now(),
        )

        val goal2 = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목2",
            deadline = LocalDate.now(),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            lastCommentReadAt = LocalDateTime.now(),
        )
        val lifeMap = lifeMapRepository.findFirstByUserId(USER_FIXTURE.id!!)
            ?: fail()

        lifeMap.addGoal(goal1)
        lifeMap.addGoal(goal2)

        // when
        val savedLifeMap = lifeMapService.findFirstByUserId(USER_FIXTURE.id!!)

        // then
        assertAll(
            Executable {
                assertThat(savedLifeMap.goalsCount).isEqualTo(2)
                assertThat(savedLifeMap.goals[0].tagContent).isEqualTo(goal1.tag.content)
                assertThat(savedLifeMap.goals[1].tagContent).isEqualTo(goal2.tag.content)
            },
        )
    }

    @Test
    @DisplayName("UserName을 통해 다른 유저의 Goals를 조회할 수 있다.")
    @Transactional
    fun findAllByUserNameTest() {
        // given
        val goal1 = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목1",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            lastCommentReadAt = LocalDateTime.now(),
        )

        val goal2 = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목2",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            lastCommentReadAt = LocalDateTime.now(),
        )
        val lifeMap = lifeMapRepository.findFirstByUserId(USER_FIXTURE.id!!)
            ?: fail()
        lifeMap.addGoal(goal1)
        lifeMap.addGoal(goal2)

        // when
        val savedLifeMap = lifeMapService.findFirstByUserId(USER_FIXTURE.id!!)

        // then
        assertAll(
            Executable {
                assertThat(savedLifeMap.goalsCount).isEqualTo(2)
                assertThat(savedLifeMap.goals[0].tagContent).isEqualTo(goal1.tag.content)
                assertThat(savedLifeMap.goals[1].tagContent).isEqualTo(goal2.tag.content)
            },
        )
    }

    @Test
    @DisplayName("UserName을 통해 다른 유저의 Goals 전체 조회시 공개 여부가 false라면 예외를 발생시킨다.")
    @Transactional
    fun findAllByUserNameValidateUserGoalsPublicTest() {
        // given
        val lifeMap = entityManager.find(LifeMap::class.java, LIFE_MAP_FIXTURE.id)
        lifeMap.updatePublic(false)

        // when
        // then
        assertThatThrownBy {
            lifeMapService.findFirstByUserName(USER_FIXTURE.username!!)
        }.isInstanceOf(RuntimeException::class.java)
    }

    @Test
    @DisplayName("LifeMapResponse의 deadline 포멧은 'YYYY.MM'이다.")
    @Transactional
    fun responseFormattingTest() {
        // given
        val now = LocalDate.now()
        val goal1 = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목1",
            deadline = now,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            lastCommentReadAt = LocalDateTime.now(),
        )

        val goal2 = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목2",
            deadline = now,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            lastCommentReadAt = LocalDateTime.now(),
        )
        val lifeMap = lifeMapRepository.findFirstByUserId(USER_FIXTURE.id!!)
            ?: fail()
        lifeMap.addGoal(goal1)
        lifeMap.addGoal(goal2)

        // when
        val savedLifeMap = lifeMapService.findFirstByUserId(USER_FIXTURE.id!!)

        // then
        var month = (now.monthValue).toString()
        if (month.length == 1) {
            month = "0$month"
        }
        val deadline = "${now.year}.$month"

        assertAll(
            Executable {
                assertThat(savedLifeMap.goals[0].deadline).isEqualTo(deadline)
                assertThat(savedLifeMap.goals[1].deadline).isEqualTo(deadline)
            },
        )
    }

    @Test
    @DisplayName("Goals 전체 조회시 Deadline 기준 오름차순, CreatedAt 기준 내림차순으로 정렬된다.")
    @Transactional
    fun sortGoalsTest() {
        // given
        val deadline이_내일이고_가장_처음_만들어진_객체 = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목1",
            deadline = LocalDate.now()
                .plusDays(1),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            lastCommentReadAt = LocalDateTime.now(),
        )

        val deadline이_내일이고_가장_나중에_만들어진_객체 = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목2",
            deadline = LocalDate.now()
                .plusDays(1),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            lastCommentReadAt = LocalDateTime.now(),
        )

        val deadline이_오늘인_객체 = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목2",
            deadline = LocalDate.now(),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            lastCommentReadAt = LocalDateTime.now(),
        )

        // when
        val lifeMap = lifeMapRepository.findFirstByUserId(USER_FIXTURE.id!!)
            ?: fail()

        lifeMap.addGoal(deadline이_내일이고_가장_처음_만들어진_객체)
        lifeMap.addGoal(deadline이_내일이고_가장_나중에_만들어진_객체)
        lifeMap.addGoal(deadline이_오늘인_객체)

        // then
        assertAll(
            Executable {
                assertThat(lifeMap.goals[0].id).isEqualTo(deadline이_오늘인_객체.id)
                assertThat(lifeMap.goals[1].id).isEqualTo(deadline이_내일이고_가장_나중에_만들어진_객체.id)
                assertThat(lifeMap.goals[2].id).isEqualTo(deadline이_내일이고_가장_처음_만들어진_객체.id)
            },
        )
    }

    @Test
    @DisplayName("지도의 공개 여부를 수정할 수 있다.")
    @Transactional
    fun updateGoalsPublic() {
        // given
        val publication = LIFE_MAP_FIXTURE.isPublic
        val updateGoalsPublicRequest = UpdatePublicRequest(publication.not())

        // when
        lifeMapService.updatePublic(
            userId = USER_FIXTURE.id!!,
            updateGoalsPublicRequest,
        )

        // then
        val lifeMap = entityManager.find(LifeMap::class.java, LIFE_MAP_FIXTURE.id!!)
        assertThat(publication).isNotEqualTo(lifeMap.isPublic)
    }
}
