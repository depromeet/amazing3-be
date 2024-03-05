package io.raemian.api.auth.converter

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "apple-login")
data class AppleLoginProps(
    val url: String,
    val keyPath: String,
    val clientId: String,
    val teamId: String,
    val keyId: String,
)
