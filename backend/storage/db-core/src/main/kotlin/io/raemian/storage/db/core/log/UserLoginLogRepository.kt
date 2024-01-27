package io.raemian.storage.db.core.log

import org.springframework.data.jpa.repository.JpaRepository

interface UserLoginLogRepository : JpaRepository<UserLoginLog, Long> {

    fun findByUserId(userId: Long): UserLoginLog?
}
