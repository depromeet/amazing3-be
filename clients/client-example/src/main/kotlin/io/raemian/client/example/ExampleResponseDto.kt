package io.raemian.client.example

import io.raemian.client.example.model.ExampleClientResult

internal data class ExampleResponseDto(
    val exampleResponseValue: String,
) {
    fun toResult(): io.raemian.client.example.model.ExampleClientResult {
        return io.raemian.client.example.model.ExampleClientResult(exampleResponseValue)
    }
}
