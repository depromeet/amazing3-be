package io.raemian.api.lifemap.model

import io.raemian.api.cheer.model.CheeringCountResult
import io.raemian.api.user.model.UserSubset

data class LifeMapResponse(
    val lifeMapId: Long,
    val isPublic: Boolean,
    val goals: List<GoalDto>,
    val goalsCount: Int,
    val user: UserSubset? = null,
    val count: CountResponse,
) {
    constructor(lifeMapDTO: LifeMapResult, lifeMapCountDTO: LifeMapCountResult, cheeringCount: Long) : this(
        lifeMapId = lifeMapDTO.lifeMapId,
        isPublic = lifeMapDTO.isPublic,
        goals = lifeMapDTO.goals,
        goalsCount = lifeMapDTO.goalsCount,
        user = lifeMapDTO.user,
        count = CountResponse(lifeMapCountDTO, cheeringCount),
    )

    constructor(lifeMapDTO: LifeMapResult, viewCount: Long, cheeringCountResponse: CheeringCountResult) : this(
        lifeMapId = lifeMapDTO.lifeMapId,
        isPublic = lifeMapDTO.isPublic,
        goals = lifeMapDTO.goals,
        goalsCount = lifeMapDTO.goalsCount,
        user = lifeMapDTO.user,
        count = CountResponse(viewCount, cheeringCountResponse),
    )
}
