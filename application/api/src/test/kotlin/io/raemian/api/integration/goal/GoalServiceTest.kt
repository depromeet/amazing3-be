package io.raemian.api.integration.goal

import io.raemian.api.goal.GoalService
import io.raemian.api.goal.controller.request.CreateGoalRequest
import io.raemian.api.lifemap.LifeMap
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.enums.OAuthProvider
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
class GoalServiceTest {

    companion object {
        val USER_FIXTURE = User(
            email = "dfghcvb111@naver.com",
            userName = "binaryHoHo",
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
    private lateinit var goalService: GoalService

    @Autowired
    private lateinit var goalRepository: GoalRepository

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
    @DisplayName("Goal ID를 통해 Goal을 조회 할 수 있다.")
    @Transactional
    fun getByIdTest() {
        // given
        val goal = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "내용",
            tasks = emptyList(),
        )
        goalRepository.save(goal)

        // when
        // then
        Assertions.assertThatCode {
            goalService.getById(goal.id!!)
        }.doesNotThrowAnyException()
    }

    @Test
    @DisplayName("목표 조회시, 목표 주인의 Goals 공개 여부가 false일 때, 예외를 발생시킨다.")
    @Transactional
    fun validateUserGoalsPublicTest() {
        // given
        val lifeMap = entityManager.find(LifeMap::class.java, LIFE_MAP_FIXTURE.id)
        lifeMap.updatePublic(false)

        val goal = Goal(
            lifeMap = lifeMap,
            title = "목표",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "목표 설명.",
            tasks = emptyList(),
        )
        goalRepository.save(goal)

        // when
        // then
        Assertions.assertThatThrownBy {
            goalService.getById(goal.id!!)
        }.isInstanceOf(SecurityException::class.java)
    }

    @Test
    @DisplayName("Goal을 생성할 수 있다.")
    fun createGoalTest() {
        val createGoalRequest = CreateGoalRequest(
            title = "title",
            description = "description",
            stickerId = STICKER_FIXTURE.id!!,
            tagId = TAG_FIXTURE.id!!,
            yearOfDeadline = "2023",
            monthOfDeadline = "12",
        )

        val createResponse = goalService.create(
            userId = USER_FIXTURE.id!!,
            createGoalRequest = createGoalRequest,
        )

        val goal = goalRepository.getById(createResponse.id)
        assertAll(
            Executable {
                assertThat(goal.title).isEqualTo(createGoalRequest.title)
                assertThat(goal.description).isEqualTo(createGoalRequest.description)
                assertThat(goal.sticker.id).isEqualTo(createGoalRequest.stickerId)
                assertThat(goal.tag.id).isEqualTo(createGoalRequest.tagId)
                assertThat(goal.deadline.year.toString())
                    .isEqualTo(createGoalRequest.yearOfDeadline)
                assertThat(goal.deadline.monthValue.toString())
                    .isEqualTo(createGoalRequest.monthOfDeadline)
            },
        )
    }

    @Test
    @DisplayName("Goal을 삭제할 수 있다.")
    @Transactional
    fun deleteGoalTest() {
        // given
        val goal = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "title",
            description = "",
            deadline = LocalDate.now(),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            tasks = emptyList(),
        )

        goalRepository.save(goal)

        // when
        goalService.delete(USER_FIXTURE.id!!, goal.id!!)

        // then
        assertThat(goalRepository.findById(goal.id!!).isEmpty).isTrue()
    }
}
