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

    PRIVATE_LIFE_MAP_EXCEPTION(
        HttpStatus.FORBIDDEN,
        1001,
        "유저의 인생 지도가 비공개 상태입니다.",
        LogLevel.INFO,
    ),

    MAX_GOAL_COUNT_EXCEEDED_EXCEPTION(
        HttpStatus.BAD_REQUEST,
        1002,
        "목표 최대 갯수를 초과했습니다.",
        LogLevel.INFO,
    ),
}
