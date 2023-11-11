package io.raemian.springboot.core.auth.controller

import io.raemian.springboot.core.auth.controller.v1.request.SignInRequest
import io.raemian.springboot.core.auth.controller.v1.request.SignUpRequest
import io.raemian.springboot.core.auth.domain.TokenDTO
import io.raemian.springboot.core.auth.service.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/auth/sign-up")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): String {
        authService.save(signUpRequest.email, signUpRequest.password)
        return "${signUpRequest.email}/${signUpRequest.password}"
    }

    @PostMapping("/auth/sign-in")
    fun signIn(@RequestBody signInRequest: SignInRequest): TokenDTO {
        return authService.signIn(signInRequest.email, signInRequest.password)
    }

    @GetMapping("/auth/my")
    fun my() {

    }
}