package io.raemian.api.support.error

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorInfo(
    val status: HttpStatus,
    val code: Int,
    val message: String,
    val logLevel: LogLevel,
) {
    DEFAULT_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "An Unexpected Error Has Occurred.",
        LogLevel.ERROR,
    ),
}
