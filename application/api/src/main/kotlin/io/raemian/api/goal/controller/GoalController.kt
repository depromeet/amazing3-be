package io.raemian.api.goal.controller

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.goal.CreateGoalResponse
import io.raemian.api.goal.GoalService
import io.raemian.api.goal.controller.request.CreateGoalRequest
import io.raemian.api.goal.controller.request.DeleteGoalRequest
import io.raemian.api.goal.controller.response.GoalResponse
import io.raemian.api.goal.controller.response.GoalsResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

fun String.toUri(): URI = URI.create(this)

@RestController
@RequestMapping("/goal")
class GoalController(
    private val goalService: GoalService,
) {

    @Operation(summary = "유저 목표 전체 조회 API")
    @GetMapping
    fun findAllByUserId(
        @AuthenticationPrincipal currentUser: CurrentUser,
    ): ResponseEntity<GoalsResponse> {
        val response = goalService.findAllByUserId(currentUser.id)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "목표 단건 조회 API")
    @GetMapping("/{goalId}")
    fun getByUserId(
        @PathVariable("goalId") goalId: Long,
    ): ResponseEntity<GoalResponse> =
        ResponseEntity.ok(goalService.getById(goalId))

    @Operation(summary = "목표 생성 API")
    @PostMapping
    fun create(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody createGoalRequest: CreateGoalRequest,
    ): ResponseEntity<CreateGoalResponse> {
        val response = goalService.create(currentUser.id, createGoalRequest)
        return ResponseEntity
            .created("/goal/${response.id}".toUri())
            .body(response)
    }

    @Operation(summary = "목표 삭제 API")
    @DeleteMapping
    fun delete(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody deleteGoalRequest: DeleteGoalRequest,
    ): ResponseEntity<Unit> {
        goalService.delete(currentUser.id, deleteGoalRequest)
        return ResponseEntity.noContent().build()
    }
}
