package io.raemian.client.example

import io.raemian.client.example.model.ExampleClientResult
import org.springframework.stereotype.Component

@Component
class ExampleClient internal constructor(
    private val exampleApi: io.raemian.client.example.ExampleApi,
) {
    fun example(
        exampleParameter: String,
    ): io.raemian.client.example.model.ExampleClientResult {
        val request = io.raemian.client.example.ExampleRequestDto(exampleParameter)
        return exampleApi.example(request).toResult()
    }
}
