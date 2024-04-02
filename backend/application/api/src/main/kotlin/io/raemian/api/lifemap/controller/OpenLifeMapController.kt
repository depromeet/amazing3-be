package io.raemian.api.lifemap.controller

import io.raemian.api.auth.model.CurrentUser
import io.raemian.api.cheer.service.CheeringService
import io.raemian.api.goal.controller.request.TimelinePageRequest
import io.raemian.api.goal.model.GoalTimelinePageResult
import io.raemian.api.goal.service.GoalQueryService
import io.raemian.api.lifemap.model.LifeMapResponse
import io.raemian.api.lifemap.service.LifeMapService
import io.raemian.api.support.response.ApiResponse
import io.raemian.api.support.response.OffsetPaginationResult
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/open/life-map")
class OpenLifeMapController(
    private val lifeMapService: LifeMapService,
    private val cheeringService: CheeringService,
    private val goalQueryService: GoalQueryService,
) {

    @Operation(summary = "UserName으로 인생 지도 조회 API")
    @GetMapping("/{username}")
    fun findAllByUserName(
        @AuthenticationPrincipal currentUser: CurrentUser?,
        @PathVariable("username") username: String,
    ): ResponseEntity<ApiResponse<LifeMapResponse>> {
        val lifeMap = lifeMapService.getFirstByUserName(username)
        val count = lifeMapService.addViewCount(lifeMap.lifeMapId)
        val cheeringCount = cheeringService.getCheeringCount(username)

        if (currentUser != null) {
            if (currentUser.id != lifeMap.user?.id) {
                lifeMapService.upsertLifeMapHistory(userId = currentUser.id, lifeMapId = lifeMap.lifeMapId)
            }
        }

        return ResponseEntity
            .ok(ApiResponse.success(LifeMapResponse(lifeMap, count, cheeringCount)))
    }

    @Operation(summary = "UserName으로 인생 지도 타임 라인 조회 API")
    @GetMapping("/timeline/{username}")
    fun getTimeline(
        @PathVariable("username") username: String,
        request: TimelinePageRequest,
    ): ResponseEntity<ApiResponse<OffsetPaginationResult<GoalTimelinePageResult>>> {
        val goalTimeline = goalQueryService.findAllByUsernameWithOffset(username, request)
        return ResponseEntity.ok(ApiResponse.success(goalTimeline))
    }
}
