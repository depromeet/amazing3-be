package io.raemian.api.cheer.controller

import io.raemian.api.cheer.CheeringServcie
import io.raemian.api.cheer.controller.request.CheeringSquadPagingRequest
import io.raemian.api.cheer.controller.response.CheererResponse
import io.raemian.api.support.response.ApiResponse
import io.raemian.api.support.response.PageResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cheering")
class CheeringController(
    private val cheeringServcie: CheeringServcie
) {

    @GetMapping("/squad/{lifeMapId}")
    fun findCheeringSquad(@PathVariable("lifeMapId") lifeMapId: Long, request: CheeringSquadPagingRequest): ResponseEntity<ApiResponse<PageResult<CheererResponse>>> {
        val response = cheeringServcie.findCheeringSquad(lifeMapId, request)

        return ResponseEntity.ok().body(ApiResponse.success(response))
    }
}