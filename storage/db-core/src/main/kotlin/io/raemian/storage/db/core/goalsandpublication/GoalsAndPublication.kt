package io.raemian.storage.db.core.goalsandpublication


import io.raemian.storage.db.core.goal.Goal

data class GoalsAndPublication(
    val goals: List<Goal>,
    val isGoalsPublic: Boolean,
)
