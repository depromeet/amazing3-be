package io.raemian.api.event

import io.raemian.storage.db.core.goal.GoalRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateCommentReadAtEventHandler(
    private val goalRepository: GoalRepository,
) {
    @Transactional
    @EventListener
    fun updateLastCommentReadTime(event: UpdateLastCommentReadAtEvent) {
        goalRepository.updateLastCommentReadAtByGoalId(
            event.goalId,
            event.commentReadAt,
        )
    }
}
