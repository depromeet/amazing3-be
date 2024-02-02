package io.raemian.api.cheer.controller

import io.raemian.api.cheer.CheeringServcie
import io.raemian.api.cheer.controller.request.CheeringRequest
import io.raemian.api.cheer.controller.request.CheeringSquadPagingRequest
import io.raemian.api.cheer.controller.response.CheererResponse
import io.raemian.api.cheer.controller.response.CheeringCountResponse
import io.raemian.api.support.response.ApiResponse
import io.raemian.api.support.response.PageResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cheering")
class CheeringController(
    private val cheeringServcie: CheeringServcie,
) {

    @GetMapping("/squad/{lifeMapId}")
    fun findCheeringSquad(@PathVariable("lifeMapId") lifeMapId: Long, request: CheeringSquadPagingRequest): ResponseEntity<ApiResponse<PageResult<CheererResponse>>> {
        val response = cheeringServcie.findCheeringSquad(lifeMapId, request)

        return ResponseEntity.ok().body(ApiResponse.success(response))
    }

    @GetMapping("/count/{userName}")
    fun getCheeringCount(@PathVariable("userName") userName: String): ResponseEntity<ApiResponse<CheeringCountResponse>> =
        ResponseEntity.ok().body(ApiResponse.success(cheeringServcie.getCheeringCount(userName)))

    @PostMapping
    fun getCheeringCount(@RequestBody request: CheeringRequest): ResponseEntity<ApiResponse<Unit>> {
        cheeringServcie.cheering(request)
        return ResponseEntity.ok().body(ApiResponse.success())
    }
}
