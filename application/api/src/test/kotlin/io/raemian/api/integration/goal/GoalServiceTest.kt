package io.raemian.api.integration.goal

import io.raemian.api.goal.GoalService
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.sticker.StickerImage
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
class GoalServiceTest {

    companion object {
        val USER_FIXTURE = User(
            "dfghcvb111@naver.com",
            "binaryHoHo",
            "binaryHoHoHo",
            LocalDate.MIN,
            OAuthProvider.NAVER,
            Authority.ROLE_USER,
        )

        val STICKER_FIXTURE = Sticker("sticker", StickerImage("image yeah"))
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
        entityManager.merge(STICKER_FIXTURE)
        entityManager.merge(TAG_FIXTURE)
    }

    @Test
    @DisplayName("Goal ID를 통해 Goal을 조회 할 수 있다.")
    @Transactional
    fun getByIdTest() {
        // given
        val goal = Goal(
            USER_FIXTURE,
            "짱이 될거야",
            LocalDate.MAX,
            STICKER_FIXTURE,
            TAG_FIXTURE,
            "열심히, 잘, 최선을 다해 꼭 짱이 된다.",
            emptyList(),
        )

        val savedGoal = goalRepository.save(goal)

        // when
        // then
        assertThatCode {
            goalService.getById(savedGoal.id!!)
        }.doesNotThrowAnyException()
    }

    @Test
    @DisplayName("User ID를 통해 유저가 가진 전체 Goal을 조회 할 수 있다.")
    @Transactional
    fun findAllByUserIdTest() {
        // given
        val goal1 = Goal(
            USER_FIXTURE,
            "제목1",
            LocalDate.MAX,
            STICKER_FIXTURE,
            TAG_FIXTURE,
            "",
            emptyList(),
        )

        val goal2 = Goal(
            USER_FIXTURE,
            "제목2",
            LocalDate.MAX,
            STICKER_FIXTURE,
            TAG_FIXTURE,
            "",
            emptyList(),
        )

        goalRepository.save(goal1)
        goalRepository.save(goal2)

        // when
        val savedGoals = goalService.findAllByUserId(USER_FIXTURE.id!!)

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
            USER_FIXTURE,
            "제목1",
            now,
            STICKER_FIXTURE,
            TAG_FIXTURE,
            "",
            emptyList(),
        )

        val goal2 = Goal(
            USER_FIXTURE,
            "제목2",
            now,
            STICKER_FIXTURE,
            TAG_FIXTURE,
            "",
            emptyList(),
        )

        goalRepository.save(goal1)
        goalRepository.save(goal2)

        // when
        val savedGoal = goalService.getById(goal1.id!!)
        val savedGoals = goalService.findAllByUserId(USER_FIXTURE.id!!)

        // then
        var month = (now.monthValue).toString()
        if (month.length == 1) {
            month = "0$month"
        }
        val deadline = "${now.year}.${month}"

        assertAll(
            Executable {
                assertThat(savedGoal.deadline).isEqualTo(deadline)
                assertThat(savedGoals.goals[0].deadline).isEqualTo(deadline)
                assertThat(savedGoals.goals[1].deadline).isEqualTo(deadline)
            },
        )
    }
}
