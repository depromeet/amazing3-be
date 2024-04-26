package io.raemian.admin.lifemap.model

import io.raemian.storage.db.core.lifemap.LifeMap

data class LifeMapResult(
    val id: Long,
    val isPublic: Boolean,
    val goals: List<GoalResult>,
    val goalsCount: Int,
    val viewCount: Long,
    val cheeringCount: Long,
) {
    constructor(lifeMap: LifeMap, lifeMapCount: Long, cheeringCount: Long) : this(
        id = lifeMap.id!!,
        isPublic = lifeMap.isPublic,
        goals = lifeMap.goals.map(::GoalResult),
        goalsCount = lifeMap.goals.size,
        viewCount = lifeMapCount,
        cheeringCount = cheeringCount,
    )
}
