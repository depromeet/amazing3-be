package io.raemian.api.config

import io.raemian.api.log.LogService
import io.raemian.api.support.error.ErrorInfo
import io.raemian.api.support.response.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    val logService: LogService,
) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        logService.createSlackErrorLog(e)
        return ResponseEntity(ApiResponse.error(ErrorInfo.DEFAULT_ERROR), ErrorInfo.DEFAULT_ERROR.status)
    }
}
