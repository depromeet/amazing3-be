package io.raemian.api.lifemap.model

import io.raemian.api.cheer.model.CheeringCountResult

data class CountResponse(
    val view: Long,
    val cheering: Long,
    val history: Long? = null,
) {
    constructor(lifeMapCountDTO: LifeMapCountResult, cheeringCount: Long) : this(
        view = lifeMapCountDTO.viewCount,
        cheering = cheeringCount,
        history = lifeMapCountDTO.historyCount,
    )
    constructor(viewCount: Long, cheeringCountResponse: CheeringCountResult) : this(
        view = viewCount,
        cheering = cheeringCountResponse.count,
    )
}
