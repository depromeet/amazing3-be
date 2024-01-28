package io.raemian.api.lifemap.domain

import io.raemian.api.user.domain.UserSubset

data class LifeMapResponse(
    val lifeMapId: Long,
    val isPublic: Boolean,
    val goals: List<GoalDto>,
    val goalsCount: Int,
    val user: UserSubset? = null,
    val count: CountResponse,
) {
    constructor(lifeMapDTO: LifeMapDTO, lifeMapCountDTO: LifeMapCountDTO) : this(
        lifeMapId = lifeMapDTO.lifeMapId,
        isPublic = lifeMapDTO.isPublic,
        goals = lifeMapDTO.goals,
        goalsCount = lifeMapDTO.goalsCount,
        user = lifeMapDTO.user,
        count = CountResponse(lifeMapCountDTO),
    )

    constructor(lifeMapDTO: LifeMapDTO, viewCount: Long) : this(
        lifeMapId = lifeMapDTO.lifeMapId,
        isPublic = lifeMapDTO.isPublic,
        goals = lifeMapDTO.goals,
        goalsCount = lifeMapDTO.goalsCount,
        user = lifeMapDTO.user,
        count = CountResponse(viewCount),
    )

    data class CountResponse(
        val view: Long,
        val history: Long? = null,
    ) {
        constructor(lifeMapCountDTO: LifeMapCountDTO) : this(
            view = lifeMapCountDTO.viewCount,
            history = lifeMapCountDTO.historyCount,
        )
        constructor(viewCount: Long) : this(
            view = viewCount,
        )
    }
}
