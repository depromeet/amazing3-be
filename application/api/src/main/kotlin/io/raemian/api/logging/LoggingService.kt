package io.raemian.api.logging

import io.raemian.api.logging.controller.request.CreateSlackErrorLogRequest
import io.raemian.infra.logging.enums.ErrorLocationEnum
import io.raemian.infra.logging.logger.SlackLogger
import org.springframework.stereotype.Service

@Service
class LoggingService(
    val slackLogger: SlackLogger,
) {

    private final val EMPTY_VALUE: String = "EMPTY VALUE"

    fun createSlackErrorLog(createSlackErrorLogRequest: CreateSlackErrorLogRequest) {
        slackLogger.error(
            createSlackErrorLogRequest.errorLocation,
            createSlackErrorLogRequest.appName,
            createSlackErrorLogRequest.errormessage,
            createSlackErrorLogRequest.userAgent,
            createSlackErrorLogRequest.referer,
        )
    }

    fun createSlackErrorLog(exception: Exception) {
        slackLogger.error(
            ErrorLocationEnum.BACKEND_SERVER,
            "one-bailey api",
            exception.message ?: EMPTY_VALUE,
        )
    }
}
