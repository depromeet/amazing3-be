package io.raemian.api.cheer.controller.response

import io.raemian.storage.db.core.cheer.Cheerer
import java.time.LocalDateTime

data class CheererResponse(
    val userId: Long,
    val userName: String,
    val userImageUrl: String,
    val cheeringAt: LocalDateTime?,
) {
    constructor(cheerer: Cheerer) : this(
        cheerer.userId,
        cheerer.username,
        cheerer.userImage,
        cheerer.cheeringAt,
    )
}
