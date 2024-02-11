package io.raemian.api.config

import io.raemian.api.log.LogService
import io.raemian.api.support.error.CoreApiException
import io.raemian.api.support.error.ErrorInfo
import io.raemian.api.support.response.ApiResponse
import jakarta.persistence.EntityNotFoundException
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

    @ExceptionHandler(CoreApiException::class)
    fun handleCoreApiException(e: CoreApiException): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(e.errorInfo), e.errorInfo.status)
    }

    @ExceptionHandler(NoSuchElementException::class, EntityNotFoundException::class)
    fun handleResourceNotFoundException(e: CoreApiException): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(
            ApiResponse.error(ErrorInfo.RESOURCE_NOT_FOUND),
            ErrorInfo.RESOURCE_NOT_FOUND.status,
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        logService.createSlackErrorLog(e)
        return ResponseEntity(
            ApiResponse.error(ErrorInfo.DEFAULT_ERROR),
            ErrorInfo.DEFAULT_ERROR.status,
        )
    }
}
