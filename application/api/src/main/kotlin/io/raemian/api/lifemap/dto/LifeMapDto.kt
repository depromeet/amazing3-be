package io.raemian.api.lifemap.dto

import io.raemian.api.lifemap.LifeMap

class LifeMapDto private constructor(
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
