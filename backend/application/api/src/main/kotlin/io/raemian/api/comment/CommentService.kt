package io.raemian.api.comment

import io.raemian.storage.db.core.comment.CommentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    val commentRepository: CommentRepository,
) {

    companion object {
        const val CONTENT_CHARACTER_LIMIT = 50
    }

    @Transactional(readOnly = true)
    fun getAllByGoalId(goalId: Long, currentUserId: Long) =
        CommentsResponse.from(commentRepository.findAllByGoalId(goalId), currentUserId)
}
