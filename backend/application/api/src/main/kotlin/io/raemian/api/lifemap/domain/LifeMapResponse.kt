package io.raemian.api.lifemap.domain

import io.raemian.api.user.domain.UserSubset

data class LifeMapResponse(
    val lifeMapId: Long,
    val isPublic: Boolean,
    val goals: List<GoalDto>,
    val goalsCount: Int,
    val user: UserSubset? = null,
    val view: ViewResponse,
) {
    constructor(lifeMapDTO: LifeMapDTO, count: Long) : this(
        lifeMapId = lifeMapDTO.lifeMapId,
        isPublic = lifeMapDTO.isPublic,
        goals = lifeMapDTO.goals,
        goalsCount = lifeMapDTO.goalsCount,
        user = lifeMapDTO.user,
        view = ViewResponse(count),
    )

    data class ViewResponse(
        val count: Long,
    )
}
