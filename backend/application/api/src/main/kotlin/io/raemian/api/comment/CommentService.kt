package io.raemian.api.comment

import io.raemian.api.support.error.CoreApiException
import io.raemian.api.support.error.ErrorInfo
import io.raemian.storage.db.core.comment.Comment
import io.raemian.storage.db.core.comment.CommentRepository
import io.raemian.storage.db.core.comment.GoalCommentReadTime
import io.raemian.storage.db.core.comment.GoalCommentReadTimeRepository
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CommentService(
    val commentRepository: CommentRepository,
    val goalRepository: GoalRepository,
    val userRepository: UserRepository,
    val goalCommentReadTimeRepository: GoalCommentReadTimeRepository,
) {

    companion object {
        const val CONTENT_CHARACTER_LIMIT = 50
    }

    @Transactional(readOnly = true)
    fun getAllByGoalId(goalId: Long, currentUserId: Long) =
        CommentsResponse.from(commentRepository.findAllByGoalId(goalId), currentUserId)

    @Transactional
    fun isNewComment(goalId: Long): Boolean =
        goalCommentReadTimeRepository.findByGoalId(goalId)?.let {
            return commentRepository.existsByCreatedAtGreaterThan(it.lastCommentReadTime)
        } ?: run {
            createNewGoalCommentReadTime(goalId)
            return true
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

    private fun createNewGoalCommentReadTime(goalId: Long) {
        val goal = goalRepository.getReferenceById(goalId)
        val goalCommentReadTime = GoalCommentReadTime(goal, LocalDateTime.now())
        goalCommentReadTimeRepository.save(goalCommentReadTime)
    }

    private fun validateContentCharacterLimit(content: String) {
        if (content.length > CONTENT_CHARACTER_LIMIT) {
            throw CoreApiException(ErrorInfo.COMMENT_CHARACTER_LIMIT_EXCEED)
        }
    }
}
