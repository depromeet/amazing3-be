package io.raemian.api.log

import io.raemian.storage.db.core.log.UserLoginLog
import io.raemian.storage.db.core.log.UserLoginLogRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserLoginLogService(
    private val userLoginLogRepository: UserLoginLogRepository,
) {
    @Async
    fun upsertLatestLogin(userId: Long?) {
        if (userId == null) {
            return
        }

        val userLoginLog = userLoginLogRepository.findByUserId(userId)
            ?: UserLoginLog(userId = userId, latestLoginAt = LocalDateTime.now())

        userLoginLogRepository.save(userLoginLog.updateLatestLogin())
    }
}
