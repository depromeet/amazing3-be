package io.raemian.api.lifemap.domain

import io.raemian.api.cheer.controller.response.CheeringCountResponse

data class CountResponse(
    val view: Long,
    val cheering: Long,
    val history: Long? = null,
) {
    constructor(lifeMapCountDTO: LifeMapCountDTO, cheeringCount: Long) : this(
        view = lifeMapCountDTO.viewCount,
        cheering = cheeringCount,
        history = lifeMapCountDTO.historyCount,
    )
    constructor(viewCount: Long, cheeringCountResponse: CheeringCountResponse) : this(
        view = viewCount,
        cheering = cheeringCountResponse.count,
    )
}
