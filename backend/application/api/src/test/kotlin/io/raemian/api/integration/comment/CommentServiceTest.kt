package io.raemian.api.integration.comment

import io.raemian.api.comment.CommentService
import io.raemian.api.comment.controller.request.WriteCommentRequest
import io.raemian.api.support.CoreApiExceptionTestSupporter.Companion.assertThrowsCoreApiExceptionExactly
import io.raemian.api.support.error.CoreApiException
import io.raemian.api.support.error.ErrorInfo
import io.raemian.storage.db.core.comment.Comment
import io.raemian.storage.db.core.comment.CommentRepository
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.enums.OAuthProvider
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Transactional
@SpringBootTest
class CommentServiceTest {

    companion object {
        val USER_FIXTURE = User(
            email = "dfghcvb111@naver.com",
            username = "binaryHoHo",
            nickname = "binaryHoHoHo",
            image = "",
            birth = LocalDate.MIN,
            provider = OAuthProvider.NAVER,
            authority = Authority.ROLE_USER,
        )

        val LIFE_MAP_FIXTURE = LifeMap(USER_FIXTURE, true)
        val STICKER_FIXTURE = Sticker("sticker", "image yeah")
        val TAG_FIXTURE = Tag("꿈")
        val GOAL_FIXTURE = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "title",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "description",
            lastCommentReadAt = LocalDateTime.now(),
        )
    }

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @BeforeEach
    fun saveEntities() {
        entityManager.merge(USER_FIXTURE)
        entityManager.merge(LIFE_MAP_FIXTURE)
        entityManager.merge(STICKER_FIXTURE)
        entityManager.merge(TAG_FIXTURE)
        goalRepository.save(GOAL_FIXTURE)
    }

    @Test
    @DisplayName("Goal의 Comment들을 조회할 수 있다")
    fun getAllByGoalIdTest() {
        // given
        commentRepository.save(Comment(GOAL_FIXTURE, USER_FIXTURE, "comment0"))
        commentRepository.save(Comment(GOAL_FIXTURE, USER_FIXTURE, "comment1"))
        commentRepository.save(Comment(GOAL_FIXTURE, USER_FIXTURE, "comment2"))

        // when
        val (comments) = commentService.findAllByGoalId(GOAL_FIXTURE.id!!, USER_FIXTURE.id!!)

        // then
        assertThat(comments[0].content).isEqualTo("comment0")
        assertThat(comments[1].content).isEqualTo("comment1")
        assertThat(comments[2].content).isEqualTo("comment2")
    }

    @Test
    @DisplayName("Goal의 Comment들을 조회할 때, 갯수를 함께 받을 수 있다.")
    fun getAllByGoalIdCommentCountTest() {
        // given
        commentRepository.save(Comment(GOAL_FIXTURE, USER_FIXTURE, "comment0"))
        commentRepository.save(Comment(GOAL_FIXTURE, USER_FIXTURE, "comment1"))
        commentRepository.save(Comment(GOAL_FIXTURE, USER_FIXTURE, "comment2"))

        // when
        val comments = commentService.findAllByGoalId(GOAL_FIXTURE.id!!, USER_FIXTURE.id!!)

        // then
        assertThat(comments.commentCount).isEqualTo(3);
    }

    @Test
    @DisplayName("유저는 읽지 않은 Comment가 있는지 확인할 수 있다.")
    fun isNewTest() {
        // given
        val comment1 = Comment(GOAL_FIXTURE, USER_FIXTURE, "comment1")
        val comment2 = Comment(GOAL_FIXTURE, USER_FIXTURE, "comment2")
        commentRepository.save(comment1)
        commentRepository.save(comment2)

        // when
        // 처음 저장된 댓글의 시간으로 last comment read at 업데이트
        goalRepository.updateLastCommentReadAtByGoalId(GOAL_FIXTURE.id!!, comment1.createdAt!!)
        val result1 = commentService.isNewComment(GOAL_FIXTURE.id!!)

        // 세 번째로 저장된 댓글의 시간으로 last comment read at 업데이트
        goalRepository.updateLastCommentReadAtByGoalId(GOAL_FIXTURE.id!!, comment2.createdAt!!)
        val result2 = commentService.isNewComment(GOAL_FIXTURE.id!!)

        // then
        assertThat(result1).isTrue()
        assertThat(result2).isFalse()
    }

    @Test
    @DisplayName("유저는 Comment를 작성할 수 있다.")
    fun writeTest() {
        // given
        val content = "댓글"

        // when
        val request = WriteCommentRequest(content)
        commentService.write(GOAL_FIXTURE.id!!, USER_FIXTURE.id!!, request)

        // then
        val comments = commentRepository.findAllByGoalId(GOAL_FIXTURE.id!!)
        assertThat(comments.size).isEqualTo(1)
        assertThat(comments[0].content).isEqualTo(content)
        assertThat(comments[0].goal.id).isEqualTo(GOAL_FIXTURE.id)
        assertThat(comments[0].commenter.id).isEqualTo(USER_FIXTURE.id)
    }

    @Test
    @DisplayName("Comment 생성시 글자수 제한을 초과하는 경우 예외가 발생한다.")
    fun writeCommentCharacterLimitExceedTest() {
        // given
        // when
        val expectedLimit = 50
        val stringBuilder = StringBuilder("")
        repeat(expectedLimit) {
            stringBuilder.append("A")
        }

        val contentNotExceedLimit = stringBuilder.toString()
        stringBuilder.append("A")
        val contentExceedLimit = stringBuilder.toString()

        val requestNotContentLimitExceed =
            WriteCommentRequest(contentNotExceedLimit)
        val requestContentLimitExceed =
            WriteCommentRequest(contentExceedLimit)

        // then
        assertDoesNotThrow {
            commentService.write(GOAL_FIXTURE.id!!, USER_FIXTURE.id!!, requestNotContentLimitExceed)
        }

        try {
            commentService.write(GOAL_FIXTURE.id!!, USER_FIXTURE.id!!, requestContentLimitExceed)
            fail()
        } catch (coreApiException: CoreApiException) {
            assertThat(coreApiException.errorInfo)
                .isEqualTo(ErrorInfo.COMMENT_CHARACTER_LIMIT_EXCEED)
        }
    }

    @Test
    @DisplayName("자신의 Comment를 지울 수 있다.")
    fun deleteTest() {
        // given
        val comment = Comment(GOAL_FIXTURE, USER_FIXTURE, "contnet")
        commentRepository.save(comment)

        // when
        val commentsBeforeDelete = commentRepository.findAllByGoalId(GOAL_FIXTURE.id!!)
        commentService.delete(comment.id!!, USER_FIXTURE.id!!)

        // then
        val commentsAfterDelete = commentRepository.findAllByGoalId(GOAL_FIXTURE.id!!)
        assertThat(commentsBeforeDelete.size)
            .isNotEqualTo(commentsAfterDelete.size)
    }

    @Test
    @DisplayName("다른 사람의 Comment를 지우는 경우 예외가 발생한다.")
    fun deleteOthersCommentTest() {
        // given
        val comment = Comment(GOAL_FIXTURE, USER_FIXTURE, "contnet")
        commentRepository.save(comment)

        // when
        // then
        assertThrowsCoreApiExceptionExactly(
            ErrorInfo.RESOURCE_DELETE_FORBIDDEN,
        ) {
            commentService.delete(comment.id!!, USER_FIXTURE.id!! + 1L)
        }
    }
}
