package io.raemian.api.logging.controller

import io.raemian.api.logging.LoggingService
import io.raemian.api.logging.controller.request.CreateSlackErrorLogRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/log")
class LogController(
    val loggingService: LoggingService,
) {

    @PostMapping("/slack/error")
    fun createSlackErrorLog(@RequestBody createSlackErrorLogRequest: CreateSlackErrorLogRequest): ResponseEntity<Unit> {
        loggingService.createSlackErrorLog(createSlackErrorLogRequest)
        return ResponseEntity.noContent().build()
    }
}
