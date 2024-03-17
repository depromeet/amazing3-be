package io.raemian.api.cheer.controller

import io.raemian.api.cheer.controller.request.CheeringRequest
import io.raemian.api.cheer.controller.request.CheeringSquadPagingRequest
import io.raemian.api.cheer.model.CheererResult
import io.raemian.api.cheer.model.CheeringCountResult
import io.raemian.api.cheer.service.CheeringService
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
    private val cheeringService: CheeringService,
) {

    @GetMapping("/squad/{lifeMapId}")
    fun findCheeringSquad(@PathVariable("lifeMapId") lifeMapId: Long, request: CheeringSquadPagingRequest): ResponseEntity<ApiResponse<PageResult<CheererResult>>> {
        val response = cheeringService.findCheeringSquad(lifeMapId, request)

        return ResponseEntity.ok().body(ApiResponse.success(response))
    }

    @GetMapping("/count/{userName}")
    fun getCheeringCount(@PathVariable("userName") userName: String): ResponseEntity<ApiResponse<CheeringCountResult>> =
        ResponseEntity.ok().body(ApiResponse.success(cheeringService.getCheeringCount(userName)))

    @PostMapping
    fun getCheeringCount(@RequestBody request: CheeringRequest): ResponseEntity<ApiResponse<Unit>> {
        cheeringService.cheering(request)
        return ResponseEntity.ok().body(ApiResponse.success())
    }
}
