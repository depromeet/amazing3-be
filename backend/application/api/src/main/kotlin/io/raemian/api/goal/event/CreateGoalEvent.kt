package io.raemian.api.goal.event

data class CreateGoalEvent(
    val goalId: Long,
    val lifeMapId: Long,
)
