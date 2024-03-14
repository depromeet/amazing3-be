package io.raemian.api.support.constant

object WebSecurityConstant {
    val PUBLIC_URIS = arrayOf(
        "/auth/**",
        "/oauth2/**",
        "/login/**",
        "/one-baily-actuator/**",
        "/log/**",
        "/open/life-map/**",
        "/cheering/squad/**",
        "/cheering/count/**",
        // for swagger
        "/swagger*/**",
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/webjars/**",
    )
}
