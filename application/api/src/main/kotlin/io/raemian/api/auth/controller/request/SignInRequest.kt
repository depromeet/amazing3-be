package io.raemian.api.auth.controller.request

data class SignInRequest(
    val email: String,
    val password: String,
)
