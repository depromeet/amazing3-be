package io.raemian.api.integration.goal

import io.raemian.api.goal.controller.request.CreateGoalRequest
import io.raemian.api.goal.controller.request.UpdateGoalRequest
import io.raemian.api.goal.service.GoalService
import io.raemian.api.support.exception.MaxGoalCountExceededException
import io.raemian.api.support.exception.PrivateLifeMapException
import io.raemian.api.support.utils.DeadlineCreator
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.lifemap.LifeMap
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
import java.time.LocalDateTime

@SpringBootTest
class GoalServiceTest {

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
            lastCommentReadAt = LocalDateTime.now(),
        )
        goalRepository.save(goal)

        // when
        // then
        Assertions.assertThatCode {
            goalService.getById(goal.id!!, USER_FIXTURE.id!!)
        }.doesNotThrowAnyException()
    }

    @Test
    @DisplayName("목표 조회시, 목표가 다른 유저의 목표이고, Goals 공개 여부가 false일 때, 예외를 발생시킨다.")
    @Transactional
    fun validateAnotherUserLifeMapPublicTest() {
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
            lastCommentReadAt = LocalDateTime.now(),
        )
        goalRepository.save(goal)

        // when
        // then
        Assertions.assertThatThrownBy {
            goalService.getById(goal.id!!, USER_FIXTURE.id!! + 1)
        }.isInstanceOf(PrivateLifeMapException::class.java)
    }

    @Test
    @DisplayName("목표 조회시, 목표가 자신의 목표이면, Goals 공개 여부가 false여도 예외를 발생시키지 않는다.")
    @Transactional
    fun validateAnotherUserLifeMapPublicTest2() {
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
            lastCommentReadAt = LocalDateTime.now(),
        )
        goalRepository.save(goal)

        // when
        // then
        Assertions.assertThatCode {
            goalService.getById(goal.id!!, USER_FIXTURE.id!!)
        }.doesNotThrowAnyException()
    }

    @Test
    @DisplayName("목표 조회시, 자신의 목표인지 다른 사람의 목표인지 확인할 수 있다.")
    @Transactional
    fun isMyGoalTest() {
        // given
        val lifeMap = entityManager.find(LifeMap::class.java, LIFE_MAP_FIXTURE.id)

        val goal = Goal(
            lifeMap = lifeMap,
            title = "목표",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "목표 설명.",
            lastCommentReadAt = LocalDateTime.now(),
        )
        goalRepository.save(goal)

        // when
        val myGoal = goalService.getById(goal.id!!, USER_FIXTURE.id!!)
        val othersGoal = goalService.getById(goal.id!!, USER_FIXTURE.id!! + 1)

        // then
        assertThat(myGoal.isMyGoal).isTrue()
        assertThat(othersGoal.isMyGoal).isFalse()
    }

    @Test
    @DisplayName("Goal을 생성할 수 있다.")
    @Transactional
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
    @DisplayName("목표 생성시 LifeMap의 목표가 50개 이상이라면 예외를 발생시킨다.")
    @Transactional
    fun validateMaxGoalCountTest() {
        // given
        val lifeMap = entityManager.find(LifeMap::class.java, LIFE_MAP_FIXTURE.id)
        val createGoalRequest = CreateGoalRequest(
            title = "title",
            description = "description",
            stickerId = STICKER_FIXTURE.id!!,
            tagId = TAG_FIXTURE.id!!,
            yearOfDeadline = "2023",
            monthOfDeadline = "12",
        )

        // when
        // Goal 50개 추가
        repeat(49) {
            addNewGoalToLifeMap(lifeMap)
        }
        entityManager.merge(lifeMap)

        // when
        // then
        // 49개일 떄는 통과한다.
        Assertions.assertThatCode {
            goalService.create(USER_FIXTURE.id!!, createGoalRequest)
        }.doesNotThrowAnyException()

        // 50개일 때는 실패한다.
        Assertions.assertThatThrownBy {
            goalService.create(USER_FIXTURE.id!!, createGoalRequest)
        }.isInstanceOf(MaxGoalCountExceededException::class.java)
    }

    @Test
    @DisplayName("Goal의 정보들을 수정할 수 있다.")
    @Transactional
    fun updateGoalTest() {
        // given
        val title = "Title"
        val description = "Description"
        val year = "2000"
        val month = "05"
        val deadline = DeadlineCreator.create(year, month)

        val goal = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = title,
            description = description,
            deadline = deadline,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            lastCommentReadAt = LocalDateTime.now(),
        )
        goalRepository.save(goal)

        // when
        val newTitle = "New" + title
        val newDescription = "New" + description

        val newSticker = Sticker(
            "New" + STICKER_FIXTURE.name,
            "New" + STICKER_FIXTURE.url,
        )
        val newTag = Tag("New" + TAG_FIXTURE.content)
        entityManager.merge(newSticker)
        entityManager.merge(newTag)

        val newDeadline = deadline.plusDays(77)
        val newYear = newDeadline.year.toString()
        val newMonth = newDeadline.monthValue.toString()

        val updateGoalRequest = UpdateGoalRequest(
            title = newTitle,
            yearOfDeadline = newYear,
            monthOfDeadline = newMonth,
            stickerId = newSticker.id!!,
            tagId = newTag.id!!,
            description = newDescription,
        )
        goalService.update(USER_FIXTURE.id!!, goal.id!!, updateGoalRequest)

        // then
        val updatedGoal = goalRepository.getById(goal.id!!)
        assertThat(updatedGoal.title).isEqualTo(newTitle)
        assertThat(updatedGoal.description).isEqualTo(newDescription)
        assertThat(updatedGoal.deadline.year).isEqualTo(newDeadline.year)
        assertThat(updatedGoal.deadline.month).isEqualTo(newDeadline.month)
        assertThat(updatedGoal.sticker.name).isEqualTo(newSticker.name)
        assertThat(updatedGoal.tag.content).isEqualTo(newTag.content)
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
            lastCommentReadAt = LocalDateTime.now(),
        )

        goalRepository.save(goal)

        // when
        goalService.delete(USER_FIXTURE.id!!, goal.id!!)

        // then
        assertThat(goalRepository.findById(goal.id!!).isEmpty).isTrue()
    }

    private fun addNewGoalToLifeMap(lifeMap: LifeMap) {
        val goal = Goal(
            lifeMap = lifeMap,
            title = "목표",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "목표 설명",
            lastCommentReadAt = LocalDateTime.now(),
        )

        lifeMap.addGoal(goal)
    }
}
