package io.raemian.api.cheer.controller.request

data class CheeringSquadPageRequest(
    val pageSize: Int,
    val cursorId: Long?,
)
