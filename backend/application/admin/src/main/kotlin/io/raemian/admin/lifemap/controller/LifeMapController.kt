package io.raemian.admin.lifemap.controller

import io.raemian.admin.lifemap.LifeMapService
import io.raemian.admin.lifemap.controller.response.LifeMapResponse
import io.raemian.admin.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/life-map")
class LifeMapController(
    private val lifeMapService: LifeMapService,
) {
    @Operation(summary = "유저 인생 지도 조회 API")
    @GetMapping
    fun findAllByUsername(@RequestParam userId: Long): ResponseEntity<ApiResponse<LifeMapResponse>> =
        ResponseEntity.ok(ApiResponse.success(lifeMapService.findByUserId(userId)))
}
