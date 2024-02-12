package io.raemian.api.lifemap.domain

data class LifeMapExploreResponses(
    val lifeMaps: List<LifeMapExploreResponse>,
    val cursor: Cursor,
) {
    constructor(lifeMapExploreDTOS: List<LifeMapExploreDTO>) : this(
        lifeMaps = lifeMapExploreDTOS.map { LifeMapExploreResponse(it) },
        cursor = Cursor(lifeMapExploreDTOS),
    )

    data class Cursor(
        val next: Long,
        // val prev: Long
    ) {
        constructor(lifeMapExploreDTOS: List<LifeMapExploreDTO>) : this(
            next = lifeMapExploreDTOS.lastOrNull()?.lifeMapId ?: Long.MIN_VALUE,
            // prev = exploreDTOs.firstOrNull()?.lifeMapId ?: Long.MAX_VALUE
        )
    }
}
