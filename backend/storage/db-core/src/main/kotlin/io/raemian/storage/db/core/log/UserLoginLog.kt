package io.raemian.storage.db.core.log

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "USER_LOGIN_LOG")
class UserLoginLog(

    @Column
    val userId: Long,

    @Column
    var latestLoginAt: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    fun updateLatestLogin(): UserLoginLog {
        this.latestLoginAt = LocalDateTime.now()

        return this
    }
}
