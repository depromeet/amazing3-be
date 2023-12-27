package io.raemian.adminapi.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringdocConfig {

    @Value("\${springdoc.server.url}")
    private lateinit var url: String

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(apiInfo())
            .servers(listOf(apiServer()))
    }

    private fun apiInfo() = Info()
        .title("BANDIBOODI Admin API 명세")
        .description("BANDIBOODI Admin API 명세서")
        .version("v1.0.0")

    private fun apiServer() = Server().url(url)
}
