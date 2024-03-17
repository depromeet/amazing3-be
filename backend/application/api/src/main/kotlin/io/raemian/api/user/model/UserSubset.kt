package io.raemian.api.user.model

import io.raemian.storage.db.core.model.GoalExploreQueryResult
import io.raemian.storage.db.core.user.User

data class UserSubset(
    val id: Long,
    val nickname: String,
    val username: String,
    val image: String,
) {
    constructor(user: User) : this(
        id = user.id!!,
        nickname = user.nickname ?: "",
        username = user.username ?: "",
        image = user.image,
    )

    constructor(result: GoalExploreQueryResult) : this(
        id = result.userId,
        nickname = result.nickname,
        username = result.username,
        image = result.userImage,
    )
}
