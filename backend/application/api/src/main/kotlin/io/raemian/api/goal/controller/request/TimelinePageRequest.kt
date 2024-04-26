package io.raemian.api.goal.controller.request

data class TimelinePageRequest(
    val page: Int = 0,
    val size: Int = 20,
)
