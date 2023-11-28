package io.raemian.api.domain.auth

data class TokenDTO(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
)
