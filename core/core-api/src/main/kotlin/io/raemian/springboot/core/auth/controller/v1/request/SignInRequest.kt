package io.raemian.springboot.core.auth.controller.v1.request

data class SignInRequest(
    val email: String,
    val password: String
)
