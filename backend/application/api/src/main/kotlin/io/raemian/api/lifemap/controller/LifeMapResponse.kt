package io.raemian.api.lifemap.controller

import io.raemian.api.lifemap.LifeMap

data class LifeMapResponse(
    val isPublic: Boolean,
    val goals: List<GoalDto>,
    val goalsCount: Int,
) {

    constructor(lifeMap: LifeMap) : this(
        isPublic = lifeMap.isPublic,
        goals = lifeMap.goals.map(::GoalDto),
        goalsCount = lifeMap.goals.size,
    )
}
