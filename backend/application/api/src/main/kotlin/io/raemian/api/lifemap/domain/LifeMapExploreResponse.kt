package io.raemian.api.lifemap.domain

import io.raemian.api.user.domain.UserSubset

data class LifeMapExploreResponse(
    val lifeMapId: Long,
    val goals: List<GoalDto>,
    val count: CountResponse,
    val user: UserSubset?,
) {

    constructor(lifeMapExploreDTO: LifeMapExploreDTO) : this(
        lifeMapId = lifeMapExploreDTO.lifeMapId,
        goals = lifeMapExploreDTO.goals,
        user = lifeMapExploreDTO.user,
        count = CountResponse(lifeMapExploreDTO.count, 0),
    )
}
