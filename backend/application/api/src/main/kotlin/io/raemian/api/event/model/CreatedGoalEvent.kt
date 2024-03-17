package io.raemian.api.event.model

data class CreatedGoalEvent(
    val goalId: Long,
    val lifeMapId: Long,
)
