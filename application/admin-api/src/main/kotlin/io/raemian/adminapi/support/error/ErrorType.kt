package io.raemian.adminapi.support.error

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorType(val status: HttpStatus, val code: ErrorCode, val message: String, val logLevel: LogLevel) {
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.", LogLevel.ERROR),
    DUPLICATE_TAG_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "해당 태그는 이미 존재합니다.", LogLevel.ERROR),
    NO_IMAGE_NAME_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "이미지 이름이 존재하지 않습니다.", LogLevel.ERROR),
    NO_PNG_FILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "이미지 파일의 확장자가 png가 아닙니다.", LogLevel.ERROR),
}
