package io.raemian.admin.config

import io.raemian.admin.support.error.CoreApiException
import io.raemian.admin.support.error.ErrorType
import io.raemian.admin.support.response.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CoreApiException::class)
    fun handleCoreApiException(e: CoreApiException): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(e.errorType), e.errorType.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(ErrorType.DEFAULT_ERROR, e), ErrorType.DEFAULT_ERROR.status)
    }
}
