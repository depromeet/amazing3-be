package io.raemian.api.comment

import io.raemian.api.comment.event.UpdateLastCommentReadAtEvent
import io.raemian.api.support.error.CoreApiException
import io.raemian.api.support.error.ErrorInfo
import io.raemian.storage.db.core.comment.Comment
import io.raemian.storage.db.core.comment.CommentRepository
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CommentService(
    val commentRepository: CommentRepository,
    val goalRepository: GoalRepository,
    val userRepository: UserRepository,
    val applicationEventPublisher: ApplicationEventPublisher,
) {

    companion object {
        const val CONTENT_CHARACTER_LIMIT = 50
    }

    @Transactional(readOnly = true)
    fun getAllByGoalId(goalId: Long, currentUserId: Long): CommentsResponse {
        if (isCurrentUserGoalOwner(goalId, currentUserId)) {
            publishUpdateCommentReadAtEvent(goalId)
        }
        return CommentsResponse.from(commentRepository.findAllByGoalId(goalId), currentUserId)
    }

    @Transactional
    fun isNewComment(goalId: Long): Boolean {
        val goal = goalRepository.getById(goalId)
        return commentRepository.existsByCreatedAtGreaterThan(goal.lastCommentReadAt)
    }

    @Transactional
    fun write(goalId: Long, currentUserId: Long, content: String) {
        validateContentCharacterLimit(content)

        val goal = goalRepository.getReferenceById(goalId)
        val currentUser = userRepository.getReferenceById(currentUserId)
        val comment = Comment(goal, currentUser, content)
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

    private fun isCurrentUserGoalOwner(goalId: Long, currentUserId: Long): Boolean {
        val goal = goalRepository.getById(goalId)
        return currentUserId == goal.lifeMap.user.id
    }

    private fun publishUpdateCommentReadAtEvent(goalId: Long) {
        val event = UpdateLastCommentReadAtEvent(goalId, LocalDateTime.now())
        applicationEventPublisher.publishEvent(event)
    }

    private fun validateContentCharacterLimit(content: String) {
        if (content.length > CONTENT_CHARACTER_LIMIT) {
            throw CoreApiException(ErrorInfo.COMMENT_CHARACTER_LIMIT_EXCEED)
        }
    }
}
