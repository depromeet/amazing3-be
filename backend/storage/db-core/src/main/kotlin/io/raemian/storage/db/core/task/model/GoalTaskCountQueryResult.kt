package io.raemian.storage.db.core.task.model

data class GoalTaskCountQueryResult(
    val goalId: Long,
    val taskCount: Int,
)
