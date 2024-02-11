package io.raemian.api.lifemap.domain

import io.raemian.api.cheer.controller.response.CheeringCountResponse
import io.raemian.api.user.domain.UserSubset

data class LifeMapResponse(
    val lifeMapId: Long,
    val isPublic: Boolean,
    val goals: List<GoalDto>,
    val goalsCount: Int,
    val user: UserSubset? = null,
    val count: CountResponse,
) {
    constructor(lifeMapDTO: LifeMapDTO, lifeMapCountDTO: LifeMapCountDTO, cheeringCount: Long) : this(
        lifeMapId = lifeMapDTO.lifeMapId,
        isPublic = lifeMapDTO.isPublic,
        goals = lifeMapDTO.goals,
        goalsCount = lifeMapDTO.goalsCount,
        user = lifeMapDTO.user,
        count = CountResponse(lifeMapCountDTO, cheeringCount),
    )

    constructor(lifeMapDTO: LifeMapDTO, viewCount: Long, cheeringCountResponse: CheeringCountResponse) : this(
        lifeMapId = lifeMapDTO.lifeMapId,
        isPublic = lifeMapDTO.isPublic,
        goals = lifeMapDTO.goals,
        goalsCount = lifeMapDTO.goalsCount,
        user = lifeMapDTO.user,
        count = CountResponse(viewCount, cheeringCountResponse),
    )
}
