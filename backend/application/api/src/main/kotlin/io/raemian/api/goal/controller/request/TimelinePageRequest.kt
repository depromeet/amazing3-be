package io.raemian.api.goal.controller.request

data class TimelinePageRequest(
    val cursor: Long?,
    val size: Int,
)
