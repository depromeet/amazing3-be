package io.raemian.api.lifemap.controller

import io.raemian.api.lifemap.LifeMapService
import io.raemian.api.lifemap.domain.LifeMapResponse
import io.raemian.api.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class OpenLifeMapController(
    private val lifeMapService: LifeMapService,
) {

    @Operation(summary = "UserName으로 인생 지도 조회 API")
    @GetMapping("/open/life-map/{username}")
    fun findAllByUserName(@PathVariable("username") username: String): ResponseEntity<ApiResponse<LifeMapResponse>> {
        val response = lifeMapService.findFirstByUserName(username)
        return ResponseEntity
            .ok(ApiResponse.success(response))
    }
}