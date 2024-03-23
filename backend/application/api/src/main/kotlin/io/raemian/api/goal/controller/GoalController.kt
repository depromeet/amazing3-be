package io.raemian.api.goal.controller

import io.raemian.api.auth.model.CurrentUser
import io.raemian.api.goal.controller.request.CreateGoalRequest
import io.raemian.api.goal.controller.request.TimelinePageRequest
import io.raemian.api.goal.controller.request.UpdateGoalRequest
import io.raemian.api.goal.model.CreateGoalResult
import io.raemian.api.goal.model.GoalExplorePageResult
import io.raemian.api.goal.model.GoalResult
import io.raemian.api.goal.model.GoalTimelinePageResult
import io.raemian.api.goal.service.GoalQueryService
import io.raemian.api.goal.service.GoalService
import io.raemian.api.support.response.ApiResponse
import io.raemian.api.support.response.PaginationResult
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

fun String.toUri(): URI = URI.create(this)

@RestController
@RequestMapping("/goal")
class GoalController(
    private val goalService: GoalService,
) {

    @Operation(summary = "목표 단건 조회 API")
    @GetMapping("/{goalId}")
    fun getById(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @PathVariable("goalId") goalId: Long,
    ): ResponseEntity<ApiResponse<GoalResult>> =
        ResponseEntity.ok(
            ApiResponse.success(goalService.getById(goalId, currentUser.id)),
        )

    @Operation(summary = "목표 생성 API")
    @PostMapping
    fun create(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody createGoalRequest: CreateGoalRequest,
    ): ResponseEntity<ApiResponse<CreateGoalResult>> {
        val response = goalService.create(currentUser.id, createGoalRequest)
        return ResponseEntity
            .created("/goal/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @Operation(summary = "목표 수정 API")
    @PatchMapping("/{goalId}")
    fun update(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @PathVariable("goalId") goalId: Long,
        @RequestBody updateGoalRequest: UpdateGoalRequest,
    ): ResponseEntity<ApiResponse<GoalResult>> {
        val goalResponse = goalService.update(currentUser.id, goalId, updateGoalRequest)
        return ResponseEntity
            .ok(ApiResponse.success(goalResponse))
    }

    @Operation(summary = "목표 삭제 API")
    @DeleteMapping("/{goalId}")
    fun delete(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @PathVariable goalId: Long,
    ): ResponseEntity<Unit> {
        goalService.delete(currentUser.id, goalId)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "explore goal")
    @GetMapping("/explore")
    fun exploreGoals(
        @AuthenticationPrincipal currentUser: CurrentUser?,
        @RequestParam(required = false, defaultValue = Long.MAX_VALUE.toString()) cursor: Long,
    ): ResponseEntity<ApiResponse<GoalExplorePageResult>> {
        val explore = goalService.explore(goalId = cursor, userId = currentUser?.id)

        return ResponseEntity.ok()
            .body(ApiResponse.success(GoalExplorePageResult(explore)))
    }
}
