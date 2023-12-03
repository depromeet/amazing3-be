package io.raemian.api.auth.controller

import io.raemian.api.auth.controller.request.SignInRequest
import io.raemian.api.auth.controller.request.UpdateUserRequest
import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.auth.domain.TokenDTO
import io.raemian.api.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/auth/sign-in")
    fun signIn(@RequestBody signInRequest: SignInRequest): TokenDTO {
        return authService.signIn(signInRequest.email)
    }

    @GetMapping("/my")
    fun my(@AuthenticationPrincipal currentUser: CurrentUser): CurrentUser {
        return currentUser
    }

    @PutMapping("/my")
    fun update(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody updateUserRequest: UpdateUserRequest,
    ): ResponseEntity<Void> {
        authService.update(
            id = currentUser.id,
            nickname = updateUserRequest.nickname,
            birth = updateUserRequest.birth,
        )
        return ResponseEntity.ok().build()
    }
}
