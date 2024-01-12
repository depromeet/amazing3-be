package io.raemian.api.lifemap.domain

import io.raemian.api.lifemap.LifeMap
import io.raemian.storage.db.core.user.User

data class LifeMapResponse(
    val isPublic: Boolean,
    val goals: List<GoalDto>,
    val goalsCount: Int,
    val user: UserSubset? = null,
) {

    constructor(lifeMap: LifeMap) : this(
        isPublic = lifeMap.isPublic,
        goals = lifeMap.goals.map(::GoalDto),
        goalsCount = lifeMap.goals.size,
    )

    constructor(lifeMap: LifeMap, user: User) : this(
        isPublic = lifeMap.isPublic,
        goals = lifeMap.goals.map(::GoalDto),
        goalsCount = lifeMap.goals.size,
        user = UserSubset(
            nickname = user.nickname!!,
            image = user.image,
        ),
    )

    data class UserSubset(
        val nickname: String,
        val image: String,
    )
}
