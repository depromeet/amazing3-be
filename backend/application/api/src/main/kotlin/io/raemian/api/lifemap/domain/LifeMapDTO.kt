package io.raemian.api.lifemap.domain

import io.raemian.api.user.domain.UserSubset
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.user.User

data class LifeMapDTO(
    val lifeMapId: Long,
    val isPublic: Boolean,
    val goals: List<GoalDto>,
    val goalsCount: Int,
    val user: UserSubset? = null,
) {

    constructor(lifeMap: LifeMap) : this(
        lifeMapId = lifeMap.id!!,
        isPublic = lifeMap.isPublic,
        goals = lifeMap.goals.map(::GoalDto),
        goalsCount = lifeMap.goals.size,
        user = UserSubset(lifeMap.user),
    )

    constructor(lifeMap: LifeMap, user: User) : this(
        lifeMapId = lifeMap.id!!,
        isPublic = lifeMap.isPublic,
        goals = lifeMap.goals.map(::GoalDto),
        goalsCount = lifeMap.goals.size,
        user = UserSubset(
            id = user.id ?: 0,
            nickname = user.nickname!!,
            image = user.image,
        ),
    )
}
