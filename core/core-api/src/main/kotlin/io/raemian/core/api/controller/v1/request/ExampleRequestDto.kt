package io.raemian.core.api.controller.v1.request

import io.raemian.core.api.domain.ExampleData

data class ExampleRequestDto(
    val data: String,
) {
    fun toExampleData(): ExampleData {
        return ExampleData(data, data)
    }
}
