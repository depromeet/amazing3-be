package io.raemian.admin.lifemap.controller.response

import io.raemian.storage.db.core.lifemap.LifeMap

data class LifeMapResponse(
    val id: Long,
    val isPublic: Boolean,
    val goals: List<GoalResponse>,
    val goalsCount: Int,
    val viewCount: Long,
    val cheeringCount: Long,
) {
    constructor(lifeMap: LifeMap, lifeMapCount: Long, cheeringCount: Long) : this(
        id = lifeMap.id!!,
        isPublic = lifeMap.isPublic,
        goals = lifeMap.goals.map(::GoalResponse),
        goalsCount = lifeMap.goals.size,
        viewCount = lifeMapCount,
        cheeringCount = cheeringCount,
    )
}
