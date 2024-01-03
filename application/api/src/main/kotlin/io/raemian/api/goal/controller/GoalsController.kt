package io.raemian.api.goal.controller

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.goal.GoalReadService
import io.raemian.api.goal.controller.response.GoalsResponse
import io.raemian.api.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/goals")
class GoalsController(
    private val goalReadService: GoalReadService,
) {

    @Operation(summary = "로그인된 유저의 전체 목표 조회 API")
    @GetMapping
    fun findAllByUserId(
        @AuthenticationPrincipal currentUser: CurrentUser,
    ): ResponseEntity<ApiResponse<GoalsResponse>> {
        val response = goalReadService.findAllByUserId(currentUser.id)
        return ResponseEntity
            .ok(ApiResponse.success(response))
    }

    @Operation(summary = "UserName으로 전체 목표 조회 API")
    @GetMapping("/{userName}")
    fun findUserGoals(@PathVariable("userName") userName: String): ResponseEntity<ApiResponse<GoalsResponse>> {
        val response = goalReadService.findAllByUserName(userName)
        return ResponseEntity
            .ok(ApiResponse.success(response))
    }
}
