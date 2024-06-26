package io.raemian.api.comment.service

import io.raemian.api.comment.controller.request.WriteCommentRequest
import io.raemian.api.comment.model.CommentsResult
import io.raemian.api.event.model.CommentReadEvent
import io.raemian.api.event.model.CreatedCommentEvent
import io.raemian.api.event.model.DeletedCommentEvent
import io.raemian.api.support.exception.CoreApiException
import io.raemian.api.support.exception.ErrorInfo
import io.raemian.storage.db.core.comment.Comment
import io.raemian.storage.db.core.comment.CommentCount
import io.raemian.storage.db.core.comment.CommentCountRepository
import io.raemian.storage.db.core.comment.CommentJdbcQueryRepository
import io.raemian.storage.db.core.comment.CommentRepository
import io.raemian.storage.db.core.comment.model.GoalCommentCountQueryResult
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val commentJdbcQueryRepository: CommentJdbcQueryRepository,
    private val commentCountRepository: CommentCountRepository,
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    @Transactional(readOnly = true)
    fun findAllByGoalId(goalId: Long, currentUserId: Long): CommentsResult {
        val comments = commentRepository.findAllByGoalId(goalId)
            .ifEmpty {
                return CommentsResult.from(
                    comments = emptyList(),
                    userId = currentUserId,
                    isMyGoal = isMyGoal(goalId, currentUserId),
                )
            }

        val goal = comments.first().goal
        val isMyGoal = isMyGoal(goal, currentUserId)
        if (isMyGoal) {
            publishUpdateCommentReadAtEvent(goalId)
        }
        return CommentsResult.from(comments, currentUserId, isMyGoal)
    }

    @Transactional
    fun isNewComment(goalId: Long): Boolean {
        val goal = goalRepository.getById(goalId)
        return commentRepository.existsByGoalIdAndCreatedAtGreaterThan(
            goalId = goal.id!!,
            createdAt = goal.lastCommentReadAt,
        )
    }

    @Transactional
    fun write(goalId: Long, currentUserId: Long, request: WriteCommentRequest) {
        val goal = goalRepository.getReferenceById(goalId)
        val currentUser = userRepository.getReferenceById(currentUserId)
        val comment = createComment(goal, currentUser, request.content)
        commentRepository.save(comment)

        applicationEventPublisher.publishEvent(CreatedCommentEvent(goalId))
    }

    @Transactional
    fun delete(commentId: Long, currentUserId: Long) {
        val comment = commentRepository.getById(commentId)

        if (isNotMyComment(comment, currentUserId) && isNotMyGoal(comment.goal, currentUserId)) {
            throw CoreApiException(ErrorInfo.RESOURCE_DELETE_FORBIDDEN)
        }

        commentRepository.delete(comment)
        applicationEventPublisher.publishEvent(DeletedCommentEvent(comment.goal.id!!))
    }

    @Transactional(readOnly = true)
    fun findGoalCommentCounts(goalIds: List<Long>): List<GoalCommentCountQueryResult> =
        commentJdbcQueryRepository.findAllGoalCommentCountByGoalIdIn(goalIds)

    @Transactional
    fun addDefaultCount(goalId: Long) {
        commentCountRepository.save(CommentCount(0, goalId))
    }

    private fun createComment(goal: Goal, currentUser: User, content: String): Comment {
        return try {
            Comment(goal, currentUser, content)
        } catch (exception: IllegalArgumentException) {
            throw CoreApiException(ErrorInfo.COMMENT_CHARACTER_LIMIT_EXCEED, exception.message)
        }
    }

    private fun isNotMyComment(
        comment: Comment,
        currentUserId: Long,
    ) = currentUserId != comment.commenter.id

    private fun isMyGoal(goalId: Long, userId: Long): Boolean {
        val goal = goalRepository.getById(goalId)
        return isMyGoal(goal, userId)
    }

    private fun isMyGoal(goal: Goal, userId: Long): Boolean =
        userId == goal.lifeMap.user.id

    private fun isNotMyGoal(goal: Goal, userId: Long): Boolean = !isMyGoal(goal, userId)

    private fun publishUpdateCommentReadAtEvent(goalId: Long) {
        val event = CommentReadEvent(goalId, LocalDateTime.now())
        applicationEventPublisher.publishEvent(event)
    }
}
