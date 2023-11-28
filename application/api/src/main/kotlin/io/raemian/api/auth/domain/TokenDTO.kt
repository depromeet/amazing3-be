package io.raemian.api.auth.domain

data class TokenDTO(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
)
