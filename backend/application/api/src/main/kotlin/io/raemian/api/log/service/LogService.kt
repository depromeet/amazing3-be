package io.raemian.api.log.service

import io.raemian.api.log.controller.request.CreateSlackErrorLogRequest
import io.raemian.infra.logging.enums.ErrorLocationEnum
import io.raemian.infra.logging.logger.SlackLogger
import io.raemian.storage.db.core.log.UserLoginLog
import io.raemian.storage.db.core.log.UserLoginLogRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LogService(
    private val slackLogger: SlackLogger,
    private val userLoginLogRepository: UserLoginLogRepository,
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

    @Async
    fun upsertLatestLogin(userId: Long?) {
        if (userId == null) {
            return
        }

        val userLoginLog = userLoginLogRepository.findByUserId(userId)
            ?: UserLoginLog(userId, LocalDateTime.now())

        userLoginLogRepository.save(userLoginLog.updateLatestLogin())
    }
}
