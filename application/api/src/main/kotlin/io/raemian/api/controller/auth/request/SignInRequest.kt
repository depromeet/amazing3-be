package io.raemian.api.controller.auth.request

data class SignInRequest(
    val email: String,
    val password: String,
)
