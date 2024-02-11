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

    RESOURCE_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        HttpStatus.NOT_FOUND.value(),
        "Resource Not Found",
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

    MAX_TASK_COUNT_EXCEEDED_EXCEPTION(
        HttpStatus.BAD_REQUEST,
        1003,
        "세부 목표의 최대 갯수를 초과했습니다.",
        LogLevel.INFO,
    ),

    TOO_MANY_CHEERING(
        HttpStatus.TOO_MANY_REQUESTS,
        1004,
        "같은 인생 지도는 1분에 1번만 응원할 수 있습니다.",
        LogLevel.INFO,
    ),
}
