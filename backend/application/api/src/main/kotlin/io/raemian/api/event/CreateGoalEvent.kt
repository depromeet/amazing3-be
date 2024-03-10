package io.raemian.api.event

data class CreateGoalEvent(
    val goalId: Long,
    val lifeMapId: Long,
)
