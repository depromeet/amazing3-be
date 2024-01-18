package io.raemian.api.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
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
            .components(Components().addSecuritySchemes("bearerAuth", apiSecurityScheme()))
            .security(apiSecurityRequirementList())
            .info(apiInfo())
            .servers(listOf(apiServer()))
    }

    private fun apiInfo() = Info()
        .title("BANDIBOODI API 명세")
        .description("BANDIBOODI API 명세서")
        .version("v1.0.0")

    private fun apiServer() = Server().url(url)

    private fun apiSecurityScheme() = SecurityScheme()
        .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
        .`in`(SecurityScheme.In.HEADER).name("Authorization")

    private fun apiSecurityRequirementList() = listOf(SecurityRequirement().addList("bearerAuth"))
}
