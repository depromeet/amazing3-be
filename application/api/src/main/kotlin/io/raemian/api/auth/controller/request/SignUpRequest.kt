package io.raemian.api.auth.controller.request

data class SignUpRequest(
    val email: String,
    val password: String,
)
