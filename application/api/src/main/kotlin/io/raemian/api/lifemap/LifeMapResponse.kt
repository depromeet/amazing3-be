package io.raemian.api.lifemap

import io.raemian.api.lifemap.dto.LifeMapDto

data class LifeMapResponse(
    val lifeMaps: List<LifeMapDto>,
)
