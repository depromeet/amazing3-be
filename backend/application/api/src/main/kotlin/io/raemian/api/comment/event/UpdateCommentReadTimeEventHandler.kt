package io.raemian.api.comment.event

import io.raemian.storage.db.core.comment.GoalCommentReadTime
import io.raemian.storage.db.core.comment.GoalCommentReadTimeRepository
import io.raemian.storage.db.core.goal.GoalRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.time.LocalDateTime

@Service
class UpdateCommentReadTimeEventHandler(
    private val goalRepository: GoalRepository,
    private val goalCommentReadTimeRepository: GoalCommentReadTimeRepository,
) {
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun updateLastCommentReadTime(event: UpdateLastCommentReadTimeEvent) {
        val goalCommentReadTime = getGoalCommentReadTime(event.goalId, event.commentReadTime)
        goalCommentReadTimeRepository.save(goalCommentReadTime)
    }

    private fun getGoalCommentReadTime(goalId: Long, commentReadTime: LocalDateTime) =
        goalCommentReadTimeRepository.findByGoalId(goalId)?.apply {
            updateLastCommentReadTime(commentReadTime)
        } ?: createNewGoalCommentReadTime(goalId, commentReadTime)

    private fun createNewGoalCommentReadTime(
        goalId: Long,
        commentReadTime: LocalDateTime,
    ): GoalCommentReadTime {
        val goal = goalRepository.getReferenceById(goalId)
        return GoalCommentReadTime(goal, commentReadTime)
    }
}
