package io.raemian.api.user.domain

import io.raemian.storage.db.core.model.GoalExploreQueryResult
import io.raemian.storage.db.core.user.User

data class UserSubset(
    val id: Long,
    val nickname: String,
    val image: String,
) {
    constructor(user: User) : this(
        id = user.id!!,
        nickname = user.nickname ?: "",
        image = user.image,
    )

    constructor(result: GoalExploreQueryResult) : this(
        id = result.userId,
        nickname = result.nickname,
        image = result.userImage,
    )
}
