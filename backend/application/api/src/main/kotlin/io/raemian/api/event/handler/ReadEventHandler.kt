package io.raemian.api.event.handler

import io.raemian.api.event.model.CommentReadEvent
import io.raemian.storage.db.core.goal.GoalRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReadEventHandler(
    private val goalRepository: GoalRepository,
) {
    @Transactional
    @EventListener
    fun updateLastCommentReadTime(event: CommentReadEvent) {
        goalRepository.updateLastCommentReadAtByGoalId(
            event.goalId,
            event.commentReadAt,
        )
    }
}
