package io.raemian.api.profile.controller

import io.raemian.api.profile.ProfileService
import io.raemian.api.profile.controller.response.DefaultProfileResponse
import io.raemian.api.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profile")
class ProfileController(
    private val profileService: ProfileService,
) {

    @Operation(summary = "기본 프로필 이미지 전체 조회 API")
    @GetMapping("/default")
    fun findAll(): ResponseEntity<ApiResponse<List<DefaultProfileResponse>>> =
        ResponseEntity.ok(
            ApiResponse.success(profileService.findAllDefault()),
        )
}
