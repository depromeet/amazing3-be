package io.raemian.api.auth.controller

import io.raemian.api.auth.controller.request.SignInRequest
import io.raemian.api.auth.controller.request.SignUpRequest
import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.auth.domain.TokenDTO
import io.raemian.api.auth.service.AuthService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
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

    @GetMapping("/my")
    fun my(@AuthenticationPrincipal currentUser: CurrentUser): CurrentUser {
        return currentUser
    }
}
