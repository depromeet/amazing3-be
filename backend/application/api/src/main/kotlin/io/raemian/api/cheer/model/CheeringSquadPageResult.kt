package io.raemian.api.cheer.model

import io.raemian.storage.db.core.common.pagination.CursorPaginationResult

data class CheeringSquadPageResult(
    val total: Long,
    val cheeringSquad: CursorPaginationResult<CheererResult>,
)
