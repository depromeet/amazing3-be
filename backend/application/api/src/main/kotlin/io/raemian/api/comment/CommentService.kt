package io.raemian.api.comment

import io.raemian.api.support.error.CoreApiException
import io.raemian.api.support.error.ErrorInfo
import io.raemian.storage.db.core.comment.Comment
import io.raemian.storage.db.core.comment.CommentRepository
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    val commentRepository: CommentRepository,
    val goalRepository: GoalRepository,
    val userRepository: UserRepository,
) {

    companion object {
        const val CONTENT_CHARACTER_LIMIT = 50
    }

    @Transactional(readOnly = true)
    fun getAllByGoalId(goalId: Long, currentUserId: Long) =
        CommentsResponse.from(commentRepository.findAllByGoalId(goalId), currentUserId)

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

    private fun validateContentCharacterLimit(content: String) {
        if (content.length > CONTENT_CHARACTER_LIMIT) {
            throw CoreApiException(ErrorInfo.COMMENT_CHARACTER_LIMIT_EXCEED)
        }
    }
}
