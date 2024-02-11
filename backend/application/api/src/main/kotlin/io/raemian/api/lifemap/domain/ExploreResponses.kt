package io.raemian.api.lifemap.domain

data class ExploreResponses(
    val lifeMaps: List<ExploreResponse>,
    val cursor: Cursor
) {
    constructor(exploreDTOs: List<ExploreDTO>): this(
        lifeMaps = exploreDTOs.map { ExploreResponse(it) },
        cursor = Cursor(exploreDTOs)
    )

    data class Cursor(
        val next: Long,
        // val prev: Long
    ) {
        constructor(exploreDTOs: List<ExploreDTO>): this(
            next = exploreDTOs.lastOrNull()?.lifeMapId ?: Long.MIN_VALUE,
            // prev = exploreDTOs.firstOrNull()?.lifeMapId ?: Long.MAX_VALUE
        )
    }
}
