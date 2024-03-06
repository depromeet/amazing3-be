package io.raemian.api.comment.event

import io.raemian.storage.db.core.goal.GoalRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class UpdateCommentReadAtEventHandler(
    private val goalRepository: GoalRepository,
) {
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun updateLastCommentReadTime(event: UpdateLastCommentReadAtEvent) =
        goalRepository.updateLastCommentReadAtByGoalId(
            event.goalId, event.commentReadAt,
        )
}
