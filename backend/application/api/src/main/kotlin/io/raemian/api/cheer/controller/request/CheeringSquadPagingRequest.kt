package io.raemian.api.cheer.controller.request

import java.time.LocalDateTime

data class CheeringSquadPagingRequest(
    val pageSize: Int,
    val lastCursorAt: LocalDateTime?,
)
