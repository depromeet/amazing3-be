package io.raemian.core.auth.controller.v1.request

data class SignUpRequest(
    val email: String,
    val password: String,
)
