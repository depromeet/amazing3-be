package io.raemian.api.controller.auth.request

data class SignUpRequest(
    val email: String,
    val password: String,
)
