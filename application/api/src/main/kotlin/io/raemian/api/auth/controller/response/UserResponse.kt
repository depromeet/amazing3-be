package io.raemian.api.auth.controller.response

import io.raemian.storage.db.core.user.User
import java.time.LocalDate
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String?,
    val nickname: String?,
    val birth: LocalDate?,
    val image: String,
    val goalCount: Int,
    val createdAt: LocalDateTime?,
) {
    companion object {
        fun of(user: User, goalCount: Int): UserResponse {
            return UserResponse(
                id = user.id!!,
                email = user.email,
                username = user.username,
                nickname = user.nickname,
                birth = user.birth,
                image = user.image,
                goalCount = goalCount,
                createdAt = user.createdAt,
            )
        }
    }
}
