package io.raemian.api.goal.controller

import io.raemian.api.goal.GoalReadService
import io.raemian.api.goal.controller.response.GoalsResponse
import io.raemian.api.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/goals")
class GoalsController(
    private val goalReadService: GoalReadService,
) {

    @Operation(summary = "UserName으로 전체 목표 조회 API")
    @GetMapping("/{username}")
    fun findUserGoals(@PathVariable("username") username: String): ResponseEntity<ApiResponse<GoalsResponse>> {
        val response = goalReadService.findAllByUsername(username)
        return ResponseEntity
            .ok(ApiResponse.success(response))
    }
}
