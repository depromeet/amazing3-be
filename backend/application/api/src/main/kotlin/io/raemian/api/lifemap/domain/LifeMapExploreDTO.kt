package io.raemian.api.lifemap.domain

import io.raemian.api.user.domain.UserSubset

data class LifeMapExploreDTO(
    val lifeMapId: Long,
    val goals: List<GoalDto>,
    val count: LifeMapCountDTO,
    val user: UserSubset?,
) {
    constructor(lifeMapDTO: LifeMapDTO, lifeMapCountDTO: LifeMapCountDTO) : this(
        lifeMapId = lifeMapDTO.lifeMapId,
        goals = lifeMapDTO.goals,
        count = lifeMapCountDTO,
        user = lifeMapDTO.user,
    )
}
