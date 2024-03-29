package io.raemian.api.user.controller

import io.raemian.api.auth.controller.request.UpdateUserInfoRequest
import io.raemian.api.auth.controller.request.UpdateUserRequest
import io.raemian.api.auth.model.CurrentUser
import io.raemian.api.lifemap.service.LifeMapService
import io.raemian.api.support.exception.ErrorInfo
import io.raemian.api.support.response.ApiResponse
import io.raemian.api.user.model.UserResult
import io.raemian.api.user.model.UserTokenDecryptResult
import io.raemian.api.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
    private val lifeMapService: LifeMapService,
) {
    @Operation(summary = "토큰 유저 정보 조회 API")
    @GetMapping("/my")
    fun my(@AuthenticationPrincipal currentUser: CurrentUser): ResponseEntity<ApiResponse<UserTokenDecryptResult>> {
        val user = userService.getUserById(currentUser.id)
        val lifeMap = lifeMapService.getFirstByUserId(currentUser.id)
        val response = UserTokenDecryptResult.of(user, lifeMap)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @Operation(summary = "유저 온보딩 이후 정보 업데이트 API")
    @PutMapping("/my")
    fun updateBaseInfo(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody updateUserRequest: UpdateUserRequest,
    ): ResponseEntity<ApiResponse<UserResult>> {
        val updated = userService.updateNicknameAndBirth(
            id = currentUser.id,
            nickname = updateUserRequest.nickname,
            birth = updateUserRequest.birth,
        )

        return ResponseEntity.ok().body(ApiResponse.success(updated))
    }

    @Operation(summary = "마이페이지 정보 업데이트 API")
    @PutMapping("/users")
    fun updateFromMy(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody updateUserInfoRequest: UpdateUserInfoRequest,
    ): ResponseEntity<ApiResponse<UserResult>> {
        val user = userService.getUserById(currentUser.id)
        if (user.username == updateUserInfoRequest.username) {
            val updated = userService.updateBaseInfo(
                id = currentUser.id,
                nickname = updateUserInfoRequest.nickname,
                birth = updateUserInfoRequest.birth,
                image = updateUserInfoRequest.image,
            )
            return ResponseEntity.ok().body(ApiResponse.success(updated))
        }

        if (!updateUserInfoRequest.validateUsername()) {
            return ResponseEntity
                .status(400)
                .body(ApiResponse.error(ErrorInfo.RESOURCE_NOT_FOUND))
        }

        if (userService.isDuplicatedUsername(updateUserInfoRequest.username)) {
            return ResponseEntity.status(409).build()
        }

        val updated = userService.update(
            id = currentUser.id,
            nickname = updateUserInfoRequest.nickname,
            birth = updateUserInfoRequest.birth,
            username = updateUserInfoRequest.username,
            image = updateUserInfoRequest.image,
        )

        return ResponseEntity.ok().body(ApiResponse.success(updated))
    }

    @Operation(summary = "유저 삭제")
    @DeleteMapping("/my")
    fun delete(@AuthenticationPrincipal currentUser: CurrentUser): ResponseEntity<Void> {
        userService.delete(currentUser.id)
        return ResponseEntity.ok().build()
    }
}
