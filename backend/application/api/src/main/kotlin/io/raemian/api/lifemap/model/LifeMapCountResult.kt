package io.raemian.api.lifemap.model

import io.raemian.storage.db.core.lifemap.LifeMapCount

data class LifeMapCountResult(
    val id: Long,
    val lifeMapId: Long,
    val viewCount: Long,
    val historyCount: Long,
) {
    constructor(lifeMapCount: LifeMapCount) : this(
        id = lifeMapCount.id!!,
        lifeMapId = lifeMapCount.lifeMapId,
        viewCount = lifeMapCount.viewCount,
        historyCount = lifeMapCount.historyCount,
    )
}
