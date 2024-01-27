package io.raemian.storage.db.core.log

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface UserLoginLogRepository : JpaRepository<UserLoginLog, Long> {

    fun findByUserId(userId: Long): UserLoginLog?
  
    fun countUserLoginLogByLatestLoginAtGreaterThanEqual(latestLoginAt: LocalDateTime): Long
}
