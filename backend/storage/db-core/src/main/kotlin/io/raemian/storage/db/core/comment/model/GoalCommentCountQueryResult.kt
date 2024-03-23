package io.raemian.storage.db.core.comment.model

data class GoalCommentCountQueryResult(
    val goalId: Long,
    val commentCount: Int,
)
