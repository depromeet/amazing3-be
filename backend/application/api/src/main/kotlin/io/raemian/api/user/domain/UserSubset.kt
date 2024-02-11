package io.raemian.api.user.domain

import io.raemian.storage.db.core.user.User

data class UserSubset(
    val id: Long,
    val nickname: String,
    val image: String,
) {
    constructor(user: User): this(
        id = user.id!!,
        nickname = user.nickname ?: "",
        image = user.image
    )
}
