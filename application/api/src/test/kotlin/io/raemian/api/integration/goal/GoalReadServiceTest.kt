package io.raemian.api.integration.goal

import io.raemian.api.goal.GoalReadService
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.enums.OAuthProvider
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
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
class GoalReadServiceTest {

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

        val STICKER_FIXTURE = Sticker("sticker", "image yeah")
        val TAG_FIXTURE = Tag("꿈")
    }

    @Autowired
    private lateinit var goalReadService: GoalReadService

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun saveEntities() {
        entityManager.merge(USER_FIXTURE)
        entityManager.merge(STICKER_FIXTURE)
        entityManager.merge(TAG_FIXTURE)
    }

    @Test
    @DisplayName("Goal ID를 통해 Goal을 조회 할 수 있다.")
    @Transactional
    fun getByIdTest() {
        // given
        val goal = Goal(
            user = USER_FIXTURE,
            title = "짱이 될거야",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "열심히, 잘, 최선을 다해 꼭 짱이 된다.",
            tasks = emptyList(),
        )

        val savedGoal = goalRepository.save(goal)

        // when
        // then
        assertThatCode {
            goalReadService.getById(savedGoal.id!!)
        }.doesNotThrowAnyException()
    }

    @Test
    @DisplayName("User ID를 통해 유저가 가진 전체 Goal을 조회 할 수 있다.")
    @Transactional
    fun findAllByUserIdTest() {
        // given
        val goal1 = Goal(
            user = USER_FIXTURE,
            title = "제목1",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            tasks = emptyList(),
        )

        val goal2 = Goal(
            user = USER_FIXTURE,
            title = "제목2",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            tasks = emptyList(),
        )

        goalRepository.save(goal1)
        goalRepository.save(goal2)

        // when
        val savedGoals = goalReadService.findAllByUserId(USER_FIXTURE.id!!)

        // then
        assertAll(
            Executable {
                assertThat(savedGoals.goalsCount).isEqualTo(2)
                assertThat(savedGoals.goals[0].tagContent).isEqualTo(goal1.tag.content)
                assertThat(savedGoals.goals[1].tagContent).isEqualTo(goal2.tag.content)
            },
        )
    }

    @Test
    @DisplayName("GoalsResponse와 GoalResponse의 deadline 포멧은 'YYYY.MM'이다.")
    @Transactional
    fun responseFormattingTest() {
        // given
        val now = LocalDate.now()
        val goal1 = Goal(
            user = USER_FIXTURE,
            title = "제목1",
            deadline = now,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            tasks = emptyList(),
        )

        val goal2 = Goal(
            user = USER_FIXTURE,
            title = "제목2",
            deadline = now,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            tasks = emptyList(),
        )
        goalRepository.save(goal1)
        goalRepository.save(goal2)

        // when
        val savedGoal = goalReadService.getById(goal1.id!!)
        val savedGoals = goalReadService.findAllByUserId(USER_FIXTURE.id!!)

        // then
        var month = (now.monthValue).toString()
        if (month.length == 1) {
            month = "0$month"
        }
        val deadline = "${now.year}.$month"

        assertAll(
            Executable {
                assertThat(savedGoal.deadline).isEqualTo(deadline)
                assertThat(savedGoals.goals[0].deadline).isEqualTo(deadline)
                assertThat(savedGoals.goals[1].deadline).isEqualTo(deadline)
            },
        )
    }

    // 정렬 테스트
    @Test
    @DisplayName("Goals 전체 조회시 Deadline 기준 오름차순, CreatedAt 기준 내림차순으로 정렬된다.")
    @Transactional
    fun sortGoalsTest() {
        // given
        val deadline이_내일이고_가장_처음_만들어진_객체 = Goal(
            user = USER_FIXTURE,
            title = "제목1",
            deadline = LocalDate.now()
                .plusDays(1),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            tasks = emptyList(),
        )

        val deadline이_내일이고_가장_나중에_만들어진_객체 = Goal(
            user = USER_FIXTURE,
            title = "제목2",
            deadline = LocalDate.now()
                .plusDays(1),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            tasks = emptyList(),
        )

        val deadline이_오늘인_객체 = Goal(
            user = USER_FIXTURE,
            title = "제목2",
            deadline = LocalDate.now(),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "",
            tasks = emptyList(),
        )

        // 역순으로 저장한다.
        goalRepository.save(deadline이_내일이고_가장_처음_만들어진_객체)
        goalRepository.save(deadline이_내일이고_가장_나중에_만들어진_객체)
        goalRepository.save(deadline이_오늘인_객체)

        // when
        val savedGoals = goalReadService.findAllByUserId(USER_FIXTURE.id!!)

        // then
        assertAll(
            Executable {
                assertThat(savedGoals.goals[0].id).isEqualTo(deadline이_오늘인_객체.id)
                assertThat(savedGoals.goals[1].id).isEqualTo(deadline이_내일이고_가장_나중에_만들어진_객체.id)
                assertThat(savedGoals.goals[2].id).isEqualTo(deadline이_내일이고_가장_처음_만들어진_객체.id)
            },
        )
    }
}
