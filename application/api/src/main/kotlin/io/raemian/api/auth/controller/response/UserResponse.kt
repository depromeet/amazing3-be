package io.raemian.api.auth.controller.response

import io.raemian.storage.db.core.user.User
import java.time.LocalDate

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String?,
    val nickname: String?,
    val birth: LocalDate?,
) {
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                email = user.email,
                username = user.userName,
                nickname = user.nickname,
                birth = user.birth,
            )
        }
    }
}