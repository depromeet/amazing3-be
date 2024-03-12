package io.raemian.api.event

data class CreatedGoalEvent(
    val goalId: Long,
    val lifeMapId: Long,
)
