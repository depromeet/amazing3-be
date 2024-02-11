package io.raemian.api.lifemap.domain

import io.raemian.api.user.domain.UserSubset

data class ExploreResponse(
    val lifeMapId: Long,
    val goals: List<GoalDto>,
    val count: CountResponse,
    val user: UserSubset?,
) {

    constructor(exploreDTO: ExploreDTO) : this(
        lifeMapId = exploreDTO.lifeMapId,
        goals = exploreDTO.goals,
        user = exploreDTO.user,
        count = CountResponse(exploreDTO.count, 0),
    )
}
