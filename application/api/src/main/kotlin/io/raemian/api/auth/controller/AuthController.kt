package io.raemian.api.auth.controller

import io.raemian.api.auth.controller.request.UpdateUserRequest
import io.raemian.api.auth.controller.response.UserResponse
import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.auth.service.AuthService
import io.raemian.api.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
) {

    @Operation(summary = "토큰 유저 정보 조회 API")
    @GetMapping("/my")
    fun my(@AuthenticationPrincipal currentUser: CurrentUser): ResponseEntity<ApiResponse<UserResponse>> {
        val user = authService.getUserById(currentUser.id)
        val response = UserResponse.of(user)

        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @Operation(summary = "유저 온보딩 이후 정보 업데이트 API")
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
