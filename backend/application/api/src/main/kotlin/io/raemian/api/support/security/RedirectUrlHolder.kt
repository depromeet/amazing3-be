package io.raemian.api.support.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "redirect-url")
data class RedirectUrlHolder(
    val live: String,
    val dev: String,
    val local: String,
)
