package io.raemian.api.lifemap.controller

import io.raemian.api.auth.model.CurrentUser
import io.raemian.api.cheer.service.CheeringService
import io.raemian.api.lifemap.controller.request.UpdatePublicRequest
import io.raemian.api.lifemap.model.LifeMapResponse
import io.raemian.api.lifemap.service.LifeMapService
import io.raemian.api.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/life-map")
class LifeMapController(
    private val lifeMapService: LifeMapService,
    private val cheeringService: CheeringService,
) {

    @Operation(summary = "로그인한 유저의 인생 지도 조회 API")
    @GetMapping
    fun findAllByCurrentUser(
        @AuthenticationPrincipal currentUser: CurrentUser,
    ): ResponseEntity<ApiResponse<LifeMapResponse>> {
        val lifeMap = lifeMapService.getFirstByUserId(currentUser.id)
        val count = lifeMapService.getLifeMapCount(lifeMap.lifeMapId)
        val cheeringCount = cheeringService.getCheeringCount(currentUser.id)

        return ResponseEntity
            .ok(ApiResponse.success(LifeMapResponse(lifeMap, count, cheeringCount)))
    }

    @Operation(summary = "인생 지도 공개 여부를 수정하는 API")
    @PatchMapping("/publication")
    fun updatePublic(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody updatePublicRequest: UpdatePublicRequest,
    ): ResponseEntity<Unit> {
        lifeMapService.updatePublic(currentUser.id, updatePublicRequest)
        return ResponseEntity.ok().build()
    }
}
