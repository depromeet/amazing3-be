package io.raemian.api.log.controller

import io.raemian.api.log.LogService
import io.raemian.api.log.controller.request.CreateSlackErrorLogRequest
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/log")
class LogController(
    val logService: LogService,
) {

    @Operation(summary = "Slack Log 생성 API")
    @PostMapping("/slack/error")
    fun createSlackErrorLog(@RequestBody createSlackErrorLogRequest: CreateSlackErrorLogRequest): ResponseEntity<Unit> {
        logService.createSlackErrorLog(createSlackErrorLogRequest)
        return ResponseEntity.noContent().build()
    }
}
