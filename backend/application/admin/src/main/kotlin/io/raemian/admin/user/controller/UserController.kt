package io.raemian.admin.user.controller

import io.raemian.admin.support.response.ApiResponse
import io.raemian.admin.user.UserService
import io.raemian.admin.user.controller.response.UserResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {
    @Operation(summary = "유저 전체 조회 API")
    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<List<UserResponse>>> =
        ResponseEntity.ok(ApiResponse.success(userService.findAll()))
}
