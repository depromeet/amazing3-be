package io.raemian.api.integration.goal

import io.raemian.api.goal.GoalService
import io.raemian.api.goal.controller.request.CreateGoalRequest
import io.raemian.api.goal.controller.request.DeleteGoalRequest
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
            "dfghcvb111@naver.com",
            "binaryHoHo",
            "binaryHoHoHo",
            LocalDate.MIN,
            OAuthProvider.NAVER,
            Authority.ROLE_USER,
        )

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
        entityManager.merge(STICKER_FIXTURE)
        entityManager.merge(TAG_FIXTURE)
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
            monthOfDeadLine = "12",
        )

        val createResponse = goalService.create(
            USER_FIXTURE.id!!,
            createGoalRequest,
        )

        val goal = goalRepository.getById(createResponse.id)
        assertAll(
            Executable {
                Assertions.assertThat(goal.title).isEqualTo(createGoalRequest.title)
                Assertions.assertThat(goal.description).isEqualTo(createGoalRequest.description)
                Assertions.assertThat(goal.sticker.id).isEqualTo(createGoalRequest.stickerId)
                Assertions.assertThat(goal.tag.id).isEqualTo(createGoalRequest.tagId)
                Assertions.assertThat(goal.deadline.year.toString())
                    .isEqualTo(createGoalRequest.yearOfDeadline)
                Assertions.assertThat(goal.deadline.monthValue.toString())
                    .isEqualTo(createGoalRequest.monthOfDeadLine)
            },
        )
    }

    @Test
    @DisplayName("Goal을 삭제할 수 있다.")
    @Transactional
    fun deleteGoalTest() {
        // given
        val goal = Goal(
            user = USER_FIXTURE,
            title = "title",
            description = "",
            deadline = LocalDate.now(),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            tasks = emptyList(),
        )

        goalRepository.save(goal)

        // when
        goalService.delete(USER_FIXTURE.id!!, DeleteGoalRequest(goal.id!!))

        // then
        assertThat(goalRepository.findById(goal.id!!).isEmpty).isTrue()
    }
}
