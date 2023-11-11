package io.raemian.springboot.core.auth.domain

data class TokenDTO(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
)
