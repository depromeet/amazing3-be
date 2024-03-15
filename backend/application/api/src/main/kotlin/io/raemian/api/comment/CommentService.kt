package io.raemian.api.comment

import io.raemian.api.comment.controller.request.WriteCommentRequest
import io.raemian.api.comment.controller.response.CommentsResponse
import io.raemian.api.event.CommentReadEvent
import io.raemian.api.support.error.CoreApiException
import io.raemian.api.support.error.ErrorInfo
import io.raemian.storage.db.core.comment.Comment
import io.raemian.storage.db.core.comment.CommentRepository
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
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    @Transactional(readOnly = true)
    fun findAllByGoalId(goalId: Long, currentUserId: Long): CommentsResponse {
        val comments = commentRepository.findAllByGoalId(goalId)
            .ifEmpty {
                return CommentsResponse.from(
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
        return CommentsResponse.from(comments, currentUserId, isMyGoal)
    }

    @Transactional
    fun isNewComment(goalId: Long): Boolean {
        val goal = goalRepository.getById(goalId)
        return commentRepository.existsByCreatedAtGreaterThan(goal.lastCommentReadAt)
    }

    @Transactional
    fun write(goalId: Long, currentUserId: Long, request: WriteCommentRequest) {
        val goal = goalRepository.getReferenceById(goalId)
        val currentUser = userRepository.getReferenceById(currentUserId)
        val comment = createComment(goal, currentUser, request.content)
        commentRepository.save(comment)
    }

    @Transactional
    fun delete(commentId: Long, currentUserId: Long) {
        val comment = commentRepository.getById(commentId)
        if (currentUserId != comment.commenter.id) {
            throw CoreApiException(ErrorInfo.RESOURCE_DELETE_FORBIDDEN)
        }

        commentRepository.delete(comment)
    }

    private fun createComment(goal: Goal, currentUser: User, content: String): Comment {
        return try {
            Comment(goal, currentUser, content)
        } catch (exception: IllegalArgumentException) {
            throw CoreApiException(ErrorInfo.COMMENT_CHARACTER_LIMIT_EXCEED, exception.message)
        }
    }

    private fun isMyGoal(goalId: Long, userId: Long): Boolean {
        val goal = goalRepository.getById(goalId)
        return isMyGoal(goal, userId)
    }

    private fun isMyGoal(goal: Goal, userId: Long): Boolean =
        userId == goal.lifeMap.user.id

    private fun publishUpdateCommentReadAtEvent(goalId: Long) {
        val event = CommentReadEvent(goalId, LocalDateTime.now())
        applicationEventPublisher.publishEvent(event)
    }
}
