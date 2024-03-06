package io.raemian.api.comment.event

import io.raemian.storage.db.core.goal.GoalRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateCommentReadAtEventHandler(
    private val goalRepository: GoalRepository,
) {
    @Async
    @Transactional
    @EventListener
    fun updateLastCommentReadTime(event: UpdateLastCommentReadAtEvent) {
        goalRepository.updateLastCommentReadAtByGoalId(
            event.goalId, event.commentReadAt,
        )
    }
}
