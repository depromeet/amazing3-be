package io.raemian.api.goal.controller.request

import java.time.LocalDateTime

data class TimelinePageRequest(
    val cursor: LocalDateTime?,
    val size: Int,
)
